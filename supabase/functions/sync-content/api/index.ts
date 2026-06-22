import * as z from "@zod/zod";
import { apiFetch } from "./fetch.ts";
import { AgentSchema, BuddySchema, CardSchema, CompetitiveSeasonSchema, ContentTierSchema, ContractSchema, CurrencySchema, EventSchema, FlexSchema, MapSchema, ModeSchema, RankTableSchema, SeasonSchema, SpraySchema, ThemeSchema, TitleSchema, VersionSchema, WeaponSchema as WeaponSchema } from "./schemas.ts";

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
	flex: () => apiFetch(`${BASE}/flex?${LANG}`, z.array(FlexSchema)),
	modes: () => apiFetch(`${BASE}/gamemodes?${LANG}`, z.array(ModeSchema)),
	maps: () => apiFetch(`${BASE}/maps?${LANG}`, z.array(MapSchema)),
	cards: () => apiFetch(`${BASE}/playercards?${LANG}`, z.array(CardSchema)),
	titles: () => apiFetch(`${BASE}/playertitles?${LANG}`, z.array(TitleSchema)),
	seasons: () => apiFetch(`${BASE}/seasons?${LANG}`, z.array(SeasonSchema)),
	competitiveSeasons: () => apiFetch(`${BASE}/seasons/competitive?${LANG}`, z.array(CompetitiveSeasonSchema)),
	sprays: () => apiFetch(`${BASE}/sprays?${LANG}`, z.array(SpraySchema)),
	weapons: () => apiFetch(`${BASE}/weapons?${LANG}`, z.array(WeaponSchema)),
	contracts: () => apiFetch(`${BASE}/contracts?${LANG}`, z.array(ContractSchema))
};