import * as z from "@zod/zod";
import { apiFetch } from "./fetch.ts";
import { AgentSchema, BuddySchema, ContentTierSchema, CurrencySchema, EventSchema, RankTableSchema, ThemeSchema, VersionSchema } from "./schemas.ts";

const BASE = "https://valorant-api.com/v1";
const LANG = "language=all";

export const api = {
	version: () => apiFetch(`${BASE}/version`, VersionSchema),
	agents: () => apiFetch(`${BASE}/agents?isPlayableCharacter=true&${LANG}`, z.array(AgentSchema)),
	themes: () => apiFetch(`${BASE}/themes?${LANG}`, z.array(ThemeSchema)),
	contentTiers: () => apiFetch(`${BASE}/contenttiers?${LANG}`, z.array(ContentTierSchema)),
	buddies: () => apiFetch(`${BASE}/buddies?${LANG}`, z.array(BuddySchema)),
	ranks: () => apiFetch(`${BASE}/competitivetiers?${LANG}`, z.array(RankTableSchema)),
	currencies: () => apiFetch(`${BASE}/currencies?${LANG}`, z.array(CurrencySchema)),
	events: () => apiFetch(`${BASE}/events?${LANG}`, z.array(EventSchema)),
};