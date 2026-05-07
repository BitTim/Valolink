import * as z from "@zod/zod";

// region:    -- envelope

export const ApiResponseSchema = <T extends z.ZodType>(dataSchema: T) =>
	z.union([
		z.object({
			status: z.literal(200),
			data: dataSchema,
		}),
		z.object({
			status: z.number(),
			error: z.string(),
		}),
	]);

// endregion: -- envelope
// region:    -- version

export const VersionSchema = z.object({
	manifestId: z.string(),
	branch: z.string(),
	version: z.string(),
	buildVersion: z.string(),
	engineVersion: z.string(),
	riotClientVersion: z.string(),
	riotClientBuild: z.string(),
	buildDate: z.iso.datetime(),
});
export type Version = z.infer<typeof VersionSchema>;

// endregion: -- version
// region:    -- agents

export const AgentRoleSchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	description: z.json(),
	displayIcon: z.url(),
});
export type AgentRole = z.infer<typeof AgentRoleSchema>;

export const AgentRecruitmentSchema = z.object({
	milestoneThreshold: z.number(),
	startDate: z.iso.datetime(),
	endDate: z.iso.datetime(),
});
export type AgentRecruitment = z.infer<typeof AgentRecruitmentSchema>;

export const AgentAbilitySchema = z.object({
	slot: z.string(),
	displayName: z.json(),
	description: z.json(),
	displayIcon: z.nullable(z.url()),
});
export type AgentAbility = z.infer<typeof AgentAbilitySchema>;

export const AgentSchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	description: z.json(),
	developerName: z.string(),
	releaseDate: z.iso.datetime(),
	displayIcon: z.url(),
	displayIconSmall: z.url(),
	bustPortrait: z.url(),
	fullPortrait: z.url(),
	fullPortraitV2: z.url(),
	killfeedPortrait: z.url(),
	minimapPortrait: z.url(),
	homeScreenPromoTileImage: z.nullable(z.url()),
	background: z.url(),
	backgroundGradientColors: z.array(z.string()),
	isFullPortraitRightFacing: z.boolean(),
	isBaseContent: z.boolean(),
	role: AgentRoleSchema,
	recruitmentData: z.nullable(AgentRecruitmentSchema),
	abilities: z.array(AgentAbilitySchema),
});
export type Agent = z.infer<typeof AgentSchema>;

// endregion: -- agents
// region:    -- themes

export const ThemeSchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	displayIcon: z.nullable(z.url()),
	storeFeaturedImage: z.nullable(z.url()),
});
export type Theme = z.infer<typeof ThemeSchema>;

// endregion: -- themes
// region:    -- content tiers

export const ContentTierSchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	devName: z.string(),
	displayIcon: z.url(),
	juiceCost: z.number(),
	juiceValue: z.number(),
	highlightColor: z.string(),
	rank: z.number(),
});
export type ContentTier = z.infer<typeof ContentTierSchema>;

// endregion: -- content tiers
// region:    -- buddies

export const BuddyLevelSchema = z.object({
	uuid: z.guid(),
	charmLevel: z.number(),
	hideIfNotOwned: z.boolean(),
	displayIcon: z.url(),
});
export type BuddyLevel = z.infer<typeof BuddyLevelSchema>;

export const BuddySchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	themeUuid: z.nullable(z.guid()),
	levels: z.array(BuddyLevelSchema),
});
export type Buddy = z.infer<typeof BuddySchema>;

// endregion: -- buddies
// region:    -- ranks

export const RankSchema = z.object({
	tier: z.number(),
	tierName: z.json(),
	division: z.string(),
	divisionName: z.json(),
	color: z.string(),
	backgroundColor: z.string(),
	smallIcon: z.nullable(z.url()),
	largeIcon: z.nullable(z.url()),
	rankTriangleDownIcon: z.nullable(z.url()),
	rankTriangleUpIcon: z.nullable(z.url()),
});
export type Rank = z.infer<typeof RankSchema>;

export const RankTableSchema = z.object({
	uuid: z.guid(),
	tiers: z.array(RankSchema),
});
export type RankTable = z.infer<typeof RankTableSchema>;

// endregion: -- ranks
// region:    -- currencies

export const CurrencySchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	displayNameSingular: z.json(),
	displayIcon: z.url(),
	largeIcon: z.url(),
	rewardPreviewIcon: z.url(),
});
export type Currency = z.infer<typeof CurrencySchema>;

// endregion: -- currencies
// region:    -- events

export const EventSchema = z.object({
	uuid: z.guid(),
	displayName: z.json(),
	shortDisplayName: z.json(),
	startTime: z.iso.datetime(),
	endTime: z.iso.datetime(),
});
export type Event = z.infer<typeof EventSchema>;

// endregion: -- events
