// import type { Agent, AgentRole } from "./api/schemas.ts";

import type { Insert } from "../_shared/db/helpers.ts";
import { Agent, Buddy, Card, CompetitiveSeason, ContentTier, Contract, Currency, Event, Flex, Map, Mode, RankTable, Season, Spray, Theme, Title, Version, Weapon } from "./api/schemas.ts";

// region:		-- helpers

function extractCategoryFromAssetPath(assetPath: string): string {
	if (assetPath.includes("NPE") || assetPath.includes("NewPlayerExperience") || assetPath.includes("BotTraining")) return "TUTORIAL";
	if (assetPath.includes("ShootingRange") || assetPath.includes("Poveglia")) return "RANGE";
	if (assetPath.includes("Deathmatch")) return "DEATHMATCH";
	if (assetPath.includes("HURM")) return "TDM";
	if (assetPath.includes("Duel")) return "SKIRMISH"

	return "STANDARD";
}

function extractCanBeRankedFromAssetPath(assetPath: string): boolean {
	return assetPath.includes("/Bomb/") || assetPath.includes("SkirmishAscension");
}

function extractWeaponStatsWallPenetration(wallPenetration: string): string {
	if (wallPenetration.includes("Low")) return "LOW";
	if (wallPenetration.includes("Medium")) return "MEDIUM";
	if (wallPenetration.includes("High")) return "HIGH";

	return "UNDEFINED";
}

function extractWeaponStatsFeature(feature: string | null): string | null {
	if (feature?.includes("Silenced")) return "SILENCED";
	if (feature?.includes("ROFIncrease")) return "ROF_INCREASE";
	if (feature?.includes("DualZoom")) return "DUAL_ZOOM";

	return null
}

function extractWeaponStatsFireMode(fireMode: string | null): string | null {
	if (fireMode?.includes("SemiAutomatic")) return "SEMI_AUTOMATIC";

	return null;
}

function extractWeaponStatsAltFireType(altFireType: string | null): string | null {
	if (altFireType?.includes("ADS")) return "ADS";
	if (altFireType?.includes("AirBurst")) return "AIR_BURST";
	if (altFireType?.includes("Shotgun")) return "SHOTGUN";

	return null;
}

function extractContractRelationType(relationType: string | null): string | null {
	if (relationType?.includes("Season")) return "SEASON";
	if (relationType?.includes("Event")) return "EVENT";
	if (relationType?.includes("Agent")) return "AGENT";
	
	return null;
}

function extractProgressionLevelRewardRelationType(type: string): string {
	if (type.includes("Spray")) return "SPRAY";
	if (type.includes("Currency")) return "CURRENCY";
	if (type.includes("EquippableSkinLevel")) return "SKIN";
	if (type.includes("PlayerCard")) return "CARD";
	if (type.includes("EquippableCharmLevel")) return "BUDDY";
	if (type.includes("Title")) return "TITLE";
	if (type.includes("Totem")) return "FLEX";

	return "UNDEFINED";
}

// endregion:	-- helpers
// region:		-- version

export function mapVersion(version: Version): Insert<"valo_version"> {
	return {
		id: 0,
		branch: version.branch,
		build_version: version.buildVersion,
		manifest_id: version.manifestId,
		riot_client_build: version.riotClientBuild,
		riot_client_version: version.riotClientVersion,
		engine_version: version.engineVersion,
		version: version.version,
		build_date: version.buildDate
	};
}

// endregion:	-- version
// region:		-- agents

export function mapAgents(apiAgents: Agent[]): {
	agentRoles: Insert<"valo_agent_roles">[],
	agentRecruitments: Insert<"valo_agent_recruitments">[],
	agentAbilities: Insert<"valo_agent_abilities">[],
	agents: Insert<"valo_agents">[]
} {
	const agentRoles: Insert<"valo_agent_roles">[] = [];
	const agentRecruitments: Insert<"valo_agent_recruitments">[] = [];
	const agentAbilities: Insert<"valo_agent_abilities">[] = [];
	const agents: Insert<"valo_agents">[] = [];

	apiAgents.forEach(agent => {
		const apiRole = agent.role;
		const apiRecruitment = agent.recruitmentData;
		const apiAbilities = agent.abilities;

		const dbRole: Insert<"valo_agent_roles"> = {
			uuid: apiRole.uuid,
			display_name: apiRole.displayName,
			description: apiRole.description,
			display_icon: apiRole.displayIcon
		};

		const dbRecruitment: Insert<"valo_agent_recruitments"> | null = apiRecruitment !== null ? {
			agent: agent.uuid,
			xp: apiRecruitment.milestoneThreshold,
			start_time: apiRecruitment.startDate,
			end_time: apiRecruitment.endDate
		} : null;

		const dbAbilities: Insert<"valo_agent_abilities">[] = apiAbilities.map(ability => ({
			agent: agent.uuid,
			slot: ability.slot,
			display_name: ability.displayName,
			description: ability.description,
			display_icon: ability.displayIcon
		}));

		const dbAgent: Insert<"valo_agents"> = {
			uuid: agent.uuid,
			display_name: agent.displayName,
			description: agent.description,
			developer_name: agent.developerName,
			release_date: agent.releaseDate,
			display_icon: agent.displayIcon,
			display_icon_small: agent.displayIconSmall,
			bust_portrait: agent.bustPortrait,
			full_portrait: agent.fullPortrait,
			full_portrait_v2: agent.fullPortraitV2,
			killfeed_portrait: agent.killfeedPortrait,
			minimap_portrait: agent.minimapPortrait,
			home_screen_promo_tile_image: agent.homeScreenPromoTileImage,
			background: agent.background,
			background_gradient_colors: agent.backgroundGradientColors,
			is_full_portrait_right_facing: agent.isFullPortraitRightFacing,
			is_base_content: agent.isBaseContent,
			role: dbRole.uuid
		};

		if (!agentRoles.some(role => role.uuid == dbRole.uuid)) agentRoles.push(dbRole);
		if (dbRecruitment !== null) agentRecruitments.push(dbRecruitment);
		agentAbilities.push(...dbAbilities);
		agents.push(dbAgent);
	});

	return {agentRoles, agentRecruitments, agentAbilities, agents};
}

// endregion:	-- agents
// region:		-- themes

export function mapThemes(apiThemes: Theme[]): Insert<"valo_themes">[] {
	return apiThemes.map(theme => ({
		uuid: theme.uuid,
		display_name: theme.displayName,
		display_icon: theme.displayIcon,
		store_featured_image: theme.storeFeaturedImage
	}));
}

// endregion:	-- themes
// region:		-- content tiers

export function mapContentTiers(apiContentTiers: ContentTier[]): Insert<"valo_content_tiers">[] {
	return apiContentTiers.map(contentTier => ({
		uuid: contentTier.uuid,
		display_name: contentTier.displayName,
		developer_name: contentTier.devName,
		display_icon: contentTier.displayIcon,
		juice_cost: contentTier.juiceCost,
		juice_value: contentTier.juiceValue,
		highlight_color: contentTier.highlightColor,
		rank: contentTier.rank
	}));
}

// endregion:	-- content tiers
// region:		-- buddies

export function mapBuddies(apiBuddies: Buddy[]): Insert<"valo_buddies">[] {
	const buddies: Insert<"valo_buddies">[] = [];

	apiBuddies.forEach(buddy => {
		const levels: Insert<"valo_buddies">[] = buddy.levels.map(level => ({
			uuid: level.uuid,
			parent: buddy.uuid,
			theme: buddy.themeUuid,
			display_name: buddy.displayName,
			charm_level: level.charmLevel,
			display_icon: level.displayIcon,
			hide_if_not_owned: level.hideIfNotOwned
		}));

		buddies.push(...levels);
	});

	return buddies;
}

// endregion:	-- buddies
// region:		-- ranks

export function mapRanks(apiRanks: RankTable[]): {
	rankTables: Insert<"valo_rank_tables">[],
	ranks: Insert<"valo_ranks">[]
} {
	const rankTables: Insert<"valo_rank_tables">[] = [];
	const ranks: Insert<"valo_ranks">[] = [];

	apiRanks.forEach(table => {
		const dbRanks: Insert<"valo_ranks">[] = table.tiers.map(rank => ({
			rank_table: table.uuid,
			tier: rank.tier,
			tier_name: rank.tierName,
			small_icon: rank.smallIcon,
			large_icon: rank.largeIcon,
			rank_triangle_down_icon: rank.rankTriangleDownIcon,
			rank_triangle_up_icon: rank.rankTriangleUpIcon,
			color: rank.color,
			background_color: rank.backgroundColor,
			division: rank.division,
			division_name: rank.divisionName
		}));

		ranks.push(...dbRanks)
		rankTables.push({ uuid: table.uuid });
	})

	return { rankTables, ranks };
}

// endregion:	-- ranks
// region:		-- currencies

export function mapCurrencoies(apiCurrencies: Currency[]): Insert<"valo_currencies">[] {
	return apiCurrencies.map(currency => ({
		uuid: currency.uuid,
		display_name: currency.displayName,
		display_name_singular: currency.displayNameSingular,
		display_icon: currency.displayIcon,
		large_icon: currency.largeIcon,
		reward_preview_icon: currency.rewardPreviewIcon
	}));
}

// endregion:	-- currencies
// region:		-- events

export function mapEvents(apiEvents: Event[]): Insert<"valo_events">[] {
	return apiEvents.map(event => ({
		uuid: event.uuid,
		display_name: event.displayName,
		short_display_name: event.shortDisplayName,
		start_time: event.startTime,
		end_time: event.endTime
	}));
}

// endregion:	-- events
// region:		-- flex

export function mapFlex(apiFlex: Flex[]): Insert<"valo_flex">[] {
	return apiFlex.map(flex => ({
		uuid: flex.uuid,
		display_name: flex.displayName,
		display_name_all_caps: flex.displayNameAllCaps,
		display_icon: flex.displayIcon
	}));
}

// endregion:	-- flex
// region:		-- modes

export function mapModes(apiModes: Mode[]): Insert<"valo_modes">[] {
	return apiModes.map(mode => ({
		uuid: mode.uuid,
		display_name: mode.displayName,
		description: mode.description,
		duration: mode.duration,
		category: extractCategoryFromAssetPath(mode.assetPath),
		display_icon: mode.displayIcon,
		list_view_icon_tall: mode.listViewIconTall,
		rounds_per_half: mode.roundsPerHalf,
		can_be_ranked: extractCanBeRankedFromAssetPath(mode.assetPath)
	}));
}

// endregion:	-- modes
// region:		-- maps

export function mapMaps(apiMaps: Map[]): Insert<"valo_maps">[] {
	return apiMaps.map(map => ({
		uuid: map.uuid,
		display_name: map.displayName,
		tactical_description: map.tacticalDescription,
		coordinates: map.coordinates,
		category: extractCategoryFromAssetPath(map.assetPath),
		list_view_icon: map.listViewIcon,
		list_view_icon_tall: map.listViewIconTall,
		splash: map.splash,
		premier_background_image: map.premierBackgroundImage,
		stylized_background_image: map.stylizedBackgroundImage,
		display_icon: map.displayIcon,
		x_multiplier: map.xMultiplier,
		x_scalar_to_add: map.xScalarToAdd,
		y_multiplier: map.yMultiplier,
		y_scalar_to_add: map.yScalarToAdd,
		callouts: map.callouts
	}));
}

// endregion:	-- maps
// region:		-- cards

export function mapCards(apiCards: Card[]): Insert<"valo_cards">[] {
	return apiCards.map(card => ({
		uuid: card.uuid,
		theme: card.themeUuid,
		display_name: card.displayName,
		display_icon: card.displayIcon,
		large_art: card.largeArt, 
		wide_art: card.wideArt, 
		small_art: card.smallArt,
		hide_if_not_owned: card.isHiddenIfNotOwned
	}));
}

// endregion:	-- cards
// region:		-- titles

export function mapTitles(apiTitles: Title[]): Insert<"valo_titles">[] {
	return apiTitles.map(title => ({
		uuid: title.uuid,
		display_name: title.displayName,
		title_text: title.titleText,
		hide_if_not_owned: title.isHiddenIfNotOwned
	}));
}

// endregion:	-- titles
// region:		-- seasons

export function mapSeasons(apiSeasons: Season[]): Insert<"valo_seasons">[] {
	const seasons: Insert<"valo_seasons">[] = [];
	const apiEpisodes = apiSeasons.filter(season => season.parentUuid === null);

	apiSeasons.forEach(season => {
		// Filter out episodes from seasons endpoint while keeping their display name
		if(season.parentUuid === null) return;

		const dbSeason: Insert<"valo_seasons"> = {
			uuid: season.uuid,
			display_name: season.displayName,
			episode_display_name: apiEpisodes.find(episode => episode.uuid == season.parentUuid)?.displayName,
			title: season.title,
			start_time: season.startTime,
			end_time: season.endTime
		};

		seasons.push(dbSeason);
	});
	
	return seasons;
}

// endregion:	-- seasons
// region:		-- competitive seasons

export function mapCompetitiveSeasons(apiCompetitiveSeasons: CompetitiveSeason[]): {
	competitiveSeasons: Insert<"valo_competitive_seasons">[],
	competitiveSeasonBorders: Insert<"valo_competitive_season_borders">[]
} {
	const competitiveSeasons: Insert<"valo_competitive_seasons">[] = [];
	const competitiveSeasonBorders: Insert<"valo_competitive_season_borders">[] = [];

	apiCompetitiveSeasons.forEach(competitiveSeason => {
		const dbCompetitiveSeason: Insert<"valo_competitive_seasons"> = {
			uuid: competitiveSeason.uuid,
			season: competitiveSeason.seasonUuid,
			rank_table: competitiveSeason.competitiveTiersUuid,
			start_time: competitiveSeason.startTime,
			end_time: competitiveSeason.endTime
		};

		competitiveSeasons.push(dbCompetitiveSeason);

		const apiBorders = competitiveSeason.borders;
		if (apiBorders === null) return;

		const dbCompetitiveSeasonBorders: Insert<"valo_competitive_season_borders">[] = apiBorders.map(border => ({
			uuid: border.uuid,
			competitive_season: competitiveSeason.uuid,
			display_icon: border.displayIcon,
			small_icon: border.smallIcon,
			level: border.level,
			wins_required: border.winsRequired
		}));

		dbCompetitiveSeasonBorders.forEach(dbBorder => {
			if(!competitiveSeasonBorders.some(border => border.uuid == dbBorder.uuid)) {
				competitiveSeasonBorders.push(dbBorder);
			}
		});
	});

	return { competitiveSeasons, competitiveSeasonBorders };
}

// endregion:	-- competitive seasons
// region:		-- sprays

export function mapSprays(apiSprays: Spray[]): Insert<"valo_sprays">[] {
	return apiSprays.map(spray => ({
		uuid: spray.uuid,
		theme: spray.themeUuid,
		display_name: spray.displayName,
		display_icon: spray.displayIcon,
		full_icon: spray.fullIcon,
		full_transparent_icon: spray.fullTransparentIcon,
		animation_png: spray.animationPng,
		animation_gif: spray.animationGif,
		hide_if_not_owned: spray.hideIfNotOwned
	}));
}

// endregion:	-- sprays
// region:		-- weapons

export function mapWeapons(apiWeapons: Weapon[]): {
	weapons: Insert<"valo_weapons">[],
	weaponStats: Insert<"valo_weapon_stats">[],
	weaponShopData: Insert<"valo_weapon_shop_data">[],
	weaponSkins: Insert<"valo_weapon_skins">[],
	weaponSkinChromas: Insert<"valo_weapon_skin_chromas">[],
	weaponSkinLevels: Insert<"valo_weapon_skin_levels">[]
} {
	const weapons: Insert<"valo_weapons">[] = [];
	const weaponStats: Insert<"valo_weapon_stats">[] = [];
	const weaponShopData: Insert<"valo_weapon_shop_data">[] = [];
	const weaponSkins: Insert<"valo_weapon_skins">[] = [];
	const weaponSkinChromas: Insert<"valo_weapon_skin_chromas">[] = [];
	const weaponSkinLevels: Insert<"valo_weapon_skin_levels">[] = [];

	apiWeapons.forEach(weapon => {
		const apiWeaponStats = weapon.weaponStats;
		const apiShopData = weapon.shopData;
		const apiSkins = weapon.skins;

		const dbWeapon: Insert<"valo_weapons"> = {
			uuid: weapon.uuid,
			display_name: weapon.displayName,
			category: weapon.category,
			default_skin: weapon.defaultSkinUuid,
			display_icon: weapon.displayIcon,
			kill_stream_icon: weapon.killStreamIcon
		};

		weapons.push(dbWeapon);

		if(apiWeaponStats !== null) {
			const dbWeaponStats: Insert<"valo_weapon_stats"> = {
				weapon: weapon.uuid,
				fire_rate: apiWeaponStats.fireRate,
				magazine_size: apiWeaponStats.magazineSize,
				run_speed_multiplier: apiWeaponStats.runSpeedMultiplier,
				equip_time_seconds: apiWeaponStats.equipTimeSeconds,
				reload_time_seconds: apiWeaponStats.reloadTimeSeconds,
				first_bullet_accuracy: apiWeaponStats.firstBulletAccuracy,
				shotgun_pellet_count: apiWeaponStats.shotgunPelletCount,
				wall_penetration: extractWeaponStatsWallPenetration(apiWeaponStats.wallPenetration),
				feature: extractWeaponStatsFeature(apiWeaponStats.feature),
				fire_mode: extractWeaponStatsFireMode(apiWeaponStats.fireMode),
				alt_fire_type: extractWeaponStatsAltFireType(apiWeaponStats.altFireType),
				ads_stats: apiWeaponStats.adsStats,
				alt_shotgun_stats: apiWeaponStats.altShotgunStats,
				air_burst_stats: apiWeaponStats.airBurstStats,
				damage_ranges: apiWeaponStats.damageRanges
			};

			weaponStats.push(dbWeaponStats);
		}

		if (apiShopData !== null) {
			const dbWeaponShopData: Insert<"valo_weapon_shop_data"> = {
				weapon: weapon.uuid,
				cost: apiShopData.cost,
				category: apiShopData.category,
				shop_order_priority: apiShopData.shopOrderPriority,
				category_text: apiShopData.categoryText,
				grid_position: apiShopData.gridPosition,
				can_be_trashed: apiShopData.canBeTrashed,
				image: apiShopData.newImage2 ?? apiShopData.newImage ?? apiShopData.image
			};

			weaponShopData.push(dbWeaponShopData);
		}

		apiSkins.forEach(skin => {
			const dbSkin: Insert<"valo_weapon_skins"> = {
				uuid: skin.uuid,
				weapon: weapon.uuid,
				display_name: skin.displayName,
				theme: skin.themeUuid,
				content_tier: skin.contentTierUuid,
				display_icon: skin.displayIcon,
				wallpaper: skin.wallpaper
			};

			const dbSkinChromas: Insert<"valo_weapon_skin_chromas">[] = skin.chromas.map(chroma => ({
				uuid: chroma.uuid,
				skin: skin.uuid,
				display_name: chroma.displayName,
				display_icon: chroma.displayIcon,
				full_render: chroma.fullRender,
				swatch: chroma.swatch,
				streamed_video: chroma.streamedVideo
			}));

			const dbSkinLevels: Insert<"valo_weapon_skin_levels">[] = skin.levels.map(level => ({
				uuid: level.uuid,
				skin: skin.uuid,
				display_name: level.displayName,
				display_icon: level.displayIcon,
				streamed_video: level.streamedVideo
			}));

			weaponSkins.push(dbSkin);
			weaponSkinChromas.push(...dbSkinChromas);
			weaponSkinLevels.push(...dbSkinLevels);
		})
	});

	return { weapons, weaponStats, weaponShopData, weaponSkins, weaponSkinChromas, weaponSkinLevels };
}

// endregion:	-- weapons
// region:		-- contracts

export function mapContracts(apiContracts: Contract[]): {
	progressions: Insert<"valo_progressions">[],
	progressionLevels: Insert<"valo_progression_levels">[],
	progressionLevelRewards: Insert<"valo_progression_level_rewards">[]
} {
	const progressions: Insert<"valo_progressions">[] = [];
	const progressionLevels: Insert<"valo_progression_levels">[] = [];
	const progressionLevelRewards: Insert<"valo_progression_level_rewards">[] = [];

	apiContracts.forEach(contract => {
		const dbProgression: Insert<"valo_progressions"> = {
			uuid: contract.uuid,
			display_name: contract.displayName,
			display_icon: contract.displayIcon,
			relation_type: extractContractRelationType(contract.content.relationType),
			relation_uuid: contract.content.relationUuid,
			premium_vp_cost: contract.content.premiumVPCost
		};

		progressions.push(dbProgression);

		let levelIndex = 0;
		contract.content.chapters.forEach(chapter => {
			let rewardSortOrder = 0;

			chapter.levels.forEach((level, i) => {
				const isLastLevel = i == chapter.levels.length - 1;

				const dbLevel: Insert<"valo_progression_levels"> = {
					progression: contract.uuid,
					level_index: levelIndex,
					xp: level.xp,
					is_purchasable_vp: level.isPurchasableWithVP,
					is_purchasable_kc: level.isPurchasableWithDough,
					vp_cost: level.vpCost,
					kc_cost: level.doughCost,
					is_epilogue: chapter.isEpilogue
				};

				const dbLevelReward: Insert<"valo_progression_level_rewards"> = {
					progression: contract.uuid,
					level_index: levelIndex,
					sort_order: rewardSortOrder++,
					relation_type: extractProgressionLevelRewardRelationType(level.reward.type),
					relation_uuid: level.reward.uuid,
					amount: level.reward.amount,
					is_free: false
				};

				progressionLevels.push(dbLevel);
				progressionLevelRewards.push(dbLevelReward);

				if (isLastLevel && chapter.freeRewards !== null) {
					const dbFreeRewards: Insert<"valo_progression_level_rewards">[] = chapter.freeRewards.map(reward => ({
						progression: contract.uuid,
						level_index: levelIndex,
						sort_order: rewardSortOrder++,
						relation_type: extractProgressionLevelRewardRelationType(reward.type),
						relation_uuid: reward.uuid,
						amount: reward.amount,
						is_free: true
					}));

					progressionLevelRewards.push(...dbFreeRewards);
				}

				levelIndex++;
			});
		})
	});

	return { progressions, progressionLevels, progressionLevelRewards };
}

// endregion:	-- contracts