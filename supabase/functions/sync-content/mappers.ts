// import type { Agent, AgentRole } from "./api/schemas.ts";

import type { Insert } from "../_shared/db/helpers.ts";
import { Agent, Buddy, ContentTier, Currency, Event, RankTable, Theme, Version } from "./api/schemas.ts";

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