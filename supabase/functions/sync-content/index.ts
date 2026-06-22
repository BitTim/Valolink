// Follow this setup guide to integrate the Deno language server with your editor:
// https://deno.land/manual/getting_started/setup_your_environment
// This enables autocomplete, go to definition, etc.

// Setup type definitions for built-in Supabase Runtime APIs
import "@supabase/functions-js/edge-runtime.d.ts"
import { api } from "./api/index.ts";
import { supabase } from "../_shared/db/client.ts";

import { PostgrestError } from "@supabase/supabase-js";
import { mapAgents, mapBuddies, mapCards, mapCompetitiveSeasons, mapContentTiers, mapContracts, mapCurrencoies as mapCurrencies, mapEvents, mapFlex, mapMaps, mapModes, mapRanks, mapSeasons, mapSprays, mapThemes, mapTitles, mapVersion, mapWeapons } from "./mappers.ts";
import { ZodError } from "@zod/zod";

Deno.serve(async (_req) => {
	try {
		// Check if new version is available
		const apiVersion = await api.version();
		const { data: dbVersion } = await supabase.from("valo_version").select().maybeSingle().throwOnError();

		if (apiVersion.version === dbVersion?.version) {
			return new Response(JSON.stringify({ message: `Already up to date; Version ${apiVersion.version}` }), { status: 200 });
		}

		// Update content
		// region:		-- agents

		const apiAgents = await api.agents();
		const { agentRoles, agentRecruitments, agentAbilities, agents } = mapAgents(apiAgents);

		console.log("Upserting agents");

		await supabase.from("valo_agent_roles").upsert(agentRoles).throwOnError();
		await supabase.from("valo_agents").upsert(agents).throwOnError();
		await supabase.from("valo_agent_recruitments").upsert(agentRecruitments).throwOnError();
		await supabase.from("valo_agent_abilities").upsert(agentAbilities).throwOnError();

		// endregion:	-- agents
		// region:		-- themes

		const apiThemes = await api.themes();
		const themes = mapThemes(apiThemes);

		console.log("Upserting themes");

		await supabase.from("valo_themes").upsert(themes).throwOnError();

		// endregion:	-- themes
		// region:		-- content tiers

		const apiContentTiers = await api.contentTiers();
		const contentTiers = mapContentTiers(apiContentTiers);

		console.log("Upserting content tiers");

		await supabase.from("valo_content_tiers").upsert(contentTiers).throwOnError();

		// endregion:	-- content tiers
		// region:		-- buddies

		const apiBuddies = await api.buddies();
		const buddies = mapBuddies(apiBuddies);

		console.log("Upserting buddies");

		await supabase.from("valo_buddies").upsert(buddies).throwOnError();

		// endregion:	-- buddies
		// region:		-- ranks

		const apiRanks = await api.ranks();
		const { rankTables, ranks } = mapRanks(apiRanks);

		console.log("Upserting ranks");

		await supabase.from("valo_rank_tables").upsert(rankTables).throwOnError();
		await supabase.from("valo_ranks").upsert(ranks).throwOnError();

		// endregion:	-- ranks
		// region:		-- currencies

		const apiCurrencies = await api.currencies();
		const currencies = mapCurrencies(apiCurrencies);

		console.log("Upserting currencies");

		await supabase.from("valo_currencies").upsert(currencies).throwOnError();

		// endregion:	-- currencies
		// region:		-- events

		const apiEvents = await api.events();
		const events = mapEvents(apiEvents);

		console.log("Upserting events");

		await supabase.from("valo_events").upsert(events).throwOnError();

		// endregion:	-- events
		// region:		-- flex

		const apiFlex = await api.flex();
		const flex = mapFlex(apiFlex);

		console.log("Upserting flex");

		await supabase.from("valo_flex").upsert(flex).throwOnError();

		// endregion:	-- flex
		// region:		-- modes

		const apiModes = await api.modes();
		const modes = mapModes(apiModes);

		console.log("Upserting modes");

		await supabase.from("valo_modes").upsert(modes).throwOnError();

		// endregion:	-- modes
		// region:		-- maps

		const apiMaps = await api.maps();
		const maps = mapMaps(apiMaps);

		console.log("Upserting maps");

		await supabase.from("valo_maps").upsert(maps).throwOnError();

		// endregion:	-- maps
		// region:		-- cards

		const apiCards = await api.cards();
		const cards = mapCards(apiCards);

		console.log("Upserting cards");

		await supabase.from("valo_cards").upsert(cards).throwOnError();

		// endregion:	-- cards
		// region:		-- titles

		const apiTitles = await api.titles();
		const titles = mapTitles(apiTitles);

		console.log("Upserting titles");

		await supabase.from("valo_titles").upsert(titles).throwOnError();

		// endregion:	-- titles
		// region:		-- seasons

		const apiSeasons = await api.seasons();
		const seasons = mapSeasons(apiSeasons);

		console.log("Upserting seasons");

		await supabase.from("valo_seasons").upsert(seasons).throwOnError();

		// endregion:	-- seasons
		// region:		-- competitive seasons

		const apiCompetitiveSeasons = await api.competitiveSeasons();
		const { competitiveSeasons, competitiveSeasonBorders } = mapCompetitiveSeasons(apiCompetitiveSeasons.filter(competitiveSeason => 
			seasons.find(season => season.uuid === competitiveSeason.seasonUuid) !== undefined
		));

		console.log("Upserting competitive seasons");

		await supabase.from("valo_competitive_seasons").upsert(competitiveSeasons).throwOnError();
		await supabase.from("valo_competitive_season_borders").upsert(competitiveSeasonBorders).throwOnError();

		// endregion:	-- competitive seasons
		// region:		-- sprays

		const apiSprays = await api.sprays();
		const sprays = mapSprays(apiSprays);

		console.log("Upserting sprays");

		await supabase.from("valo_sprays").upsert(sprays).throwOnError();

		// endregion:	-- sprays
		// region:		-- weapons

		const apiWeapons = await api.weapons();
		const { weapons, weaponStats, weaponShopData, weaponSkins, weaponSkinChromas, weaponSkinLevels } = mapWeapons(apiWeapons);

		console.log("Upserting weapons");

		await supabase.from("valo_weapons").upsert(weapons).throwOnError();
		await supabase.from("valo_weapon_stats").upsert(weaponStats).throwOnError();
		await supabase.from("valo_weapon_shop_data").upsert(weaponShopData).throwOnError();
		await supabase.from("valo_weapon_skins").upsert(weaponSkins).throwOnError();
		await supabase.from("valo_weapon_skin_chromas").upsert(weaponSkinChromas).throwOnError();
		await supabase.from("valo_weapon_skin_levels").upsert(weaponSkinLevels).throwOnError();

		// endregion:	-- weapons
		// region:		-- contracts

		const apiContracts = await api.contracts();
		const { progressions, progressionLevels, progressionLevelRewards } = mapContracts(apiContracts);

		console.log("Upserting progressions");

		await supabase.from("valo_progressions").upsert(progressions).throwOnError();
		await supabase.from("valo_progression_levels").upsert(progressionLevels).throwOnError();
		await supabase.from("valo_progression_level_rewards").upsert(progressionLevelRewards).throwOnError();

		// endregion:	-- contracts

		// Update Version
		const version = mapVersion(apiVersion);
		console.log("Upserting version")
		await supabase.from("valo_version").upsert(version).throwOnError();

		console.log("Done");
		return new Response(JSON.stringify({ message: `Synced to version ${apiVersion?.version}` }), { status: 200 });
	} catch(err) {
		if (err instanceof String) {
			console.error(err);
			return new Response(JSON.stringify({ message: err }), { status: 500 });
		}

		if (err instanceof PostgrestError || err instanceof ZodError) {
			console.error(err.message);
			return new Response(JSON.stringify({ message: err.message }), { status: 500 });
		}

		console.error(err);
		return new Response(JSON.stringify({ message: "Unknown Error" }), { status: 500 });
	}
});
