// Follow this setup guide to integrate the Deno language server with your editor:
// https://deno.land/manual/getting_started/setup_your_environment
// This enables autocomplete, go to definition, etc.

// Setup type definitions for built-in Supabase Runtime APIs
import "@supabase/functions-js/edge-runtime.d.ts"
import { api } from "./api/index.ts";
import { supabase } from "../_shared/db/client.ts";

import { PostgrestError } from "@supabase/supabase-js";
import { mapAgents, mapBuddies, mapContentTiers, mapCurrencoies as mapCurrencies, mapEvents, mapRanks, mapThemes, mapVersion } from "./mappers.ts";
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

		await supabase.from("valo_agent_roles").upsert(agentRoles).throwOnError();
		await supabase.from("valo_agents").upsert(agents).throwOnError();
		await supabase.from("valo_agent_recruitments").upsert(agentRecruitments).throwOnError();
		await supabase.from("valo_agent_abilities").upsert(agentAbilities).throwOnError();

		// endregion:	-- agents
		// region:		-- themes

		const apiThemes = await api.themes();
		const themes = mapThemes(apiThemes);

		await supabase.from("valo_themes").upsert(themes).throwOnError();

		// endregion:	-- themes
		// region:		-- content tiers

		const apiContentTiers = await api.contentTiers();
		const contentTiers = mapContentTiers(apiContentTiers);

		await supabase.from("valo_content_tiers").upsert(contentTiers).throwOnError();

		// endregion:	-- content tiers
		// region:		-- buddies

		const apiBuddies = await api.buddies();
		const buddies = mapBuddies(apiBuddies);

		await supabase.from("valo_buddies").upsert(buddies).throwOnError();

		// endregion:	-- buddies
		// region:		-- ranks

		const apiRanks = await api.ranks();
		const { rankTables, ranks } = mapRanks(apiRanks);

		await supabase.from("valo_rank_tables").upsert(rankTables).throwOnError();
		await supabase.from("valo_ranks").upsert(ranks).throwOnError();

		// endregion:	-- ranks
		// region:		-- currencies

		const apiCurrencies = await api.currencies();
		const currencies = mapCurrencies(apiCurrencies);

		await supabase.from("valo_currencies").upsert(currencies).throwOnError();

		// endregion:	-- currencies
		// region:		-- events

		const apiEvents = await api.events();
		const events = mapEvents(apiEvents);

		await supabase.from("valo_events").upsert(events).throwOnError();

		// endregion:	-- currencies

		// Update Version
		const version = mapVersion(apiVersion);
		await supabase.from("valo_version").upsert(version).throwOnError();

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
