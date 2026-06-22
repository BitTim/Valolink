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
// region:		-- events

export const EventSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  shortDisplayName: z.json(),
  startTime: z.iso.datetime(),
  endTime: z.iso.datetime(),
});
export type Event = z.infer<typeof EventSchema>;

// endregion:	-- events
// region:		-- flex

export const FlexSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  displayNameAllCaps: z.json(),
  displayIcon: z.url()
});
export type Flex = z.infer<typeof FlexSchema>;

// endregion:	-- flex
// region:    -- modes

export const ModeSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  description: z.nullable(z.json()),
  duration: z.nullable(z.json()),
  assetPath: z.string(),
  displayIcon: z.nullable(z.url()),
  listViewIconTall: z.nullable(z.url()),
  roundsPerHalf: z.number()
});
export type Mode = z.infer<typeof ModeSchema>;

// endregion: -- modes
// region:    -- maps

export const MapSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  tacticalDescription: z.nullable(z.json()),
  coordinates: z.nullable(z.json()),
  assetPath: z.string(),
  listViewIcon: z.url(),
  listViewIconTall: z.url(),
  splash: z.url(),
  premierBackgroundImage: z.nullable(z.url()),
  stylizedBackgroundImage: z.nullable(z.url()),
  displayIcon: z.nullable(z.url()),
  xMultiplier: z.float32(),
  xScalarToAdd: z.float32(),
  yMultiplier: z.float32(),
  yScalarToAdd: z.float32(),
  callouts: z.nullable(z.json())
});
export type Map = z.infer<typeof MapSchema>;

// endregion: -- maps
// region:    -- cards

export const CardSchema = z.object({
  uuid: z.guid(),
  themeUuid: z.nullable(z.guid()),
  displayName: z.json(),
  displayIcon: z.url(),
  largeArt: z.nullable(z.url()),
  wideArt: z.url(),
  smallArt: z.url(),
  isHiddenIfNotOwned: z.boolean()
});
export type Card = z.infer<typeof CardSchema>;

// endregion: -- cards
// region:    -- titles

export const TitleSchema = z.object({
  uuid: z.guid(),
  displayName: z.nullable(z.json()),
  titleText: z.nullable(z.json()),
  isHiddenIfNotOwned: z.boolean()
});
export type Title = z.infer<typeof TitleSchema>;

// endregion: -- titles
// region:    -- seasons

export const SeasonSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  title: z.nullable(z.json()),
  startTime: z.iso.datetime(),
  endTime: z.iso.datetime(),
  parentUuid: z.nullable(z.guid())
});
export type Season = z.infer<typeof SeasonSchema>;

// endregion: -- seasons
// region:    -- competitive seasons

export const CompetitiveSeasonBorderSchema = z.object({
  uuid: z.guid(),
  displayIcon: z.url(),
  smallIcon: z.nullable(z.url()),
  level: z.number(),
  winsRequired: z.number()
});
export type CompetitiveSeasonBorder = z.infer<typeof CompetitiveSeasonBorderSchema>;

export const CompetitiveSeasonSchema = z.object({
  uuid: z.guid(),
  seasonUuid: z.guid(),
  competitiveTiersUuid: z.guid(),
  startTime: z.iso.datetime(),
  endTime: z.iso.datetime(),
  borders: z.nullable(z.array(CompetitiveSeasonBorderSchema))
});
export type CompetitiveSeason = z.infer<typeof CompetitiveSeasonSchema>;

// endregion: -- competitive seasons
// region:    -- sprays

export const SpraySchema = z.object({
  uuid: z.guid(),
  themeUuid: z.nullable(z.guid()),
  displayName: z.json(),
  displayIcon: z.url(),
  fullIcon: z.nullable(z.url()),
  fullTransparentIcon: z.nullable(z.url()),
  animationPng: z.nullable(z.url()),
  animationGif: z.nullable(z.url()),
  hideIfNotOwned: z.boolean()
});
export type Spray = z.infer<typeof SpraySchema>;

// endregion: -- sprays
// region:    -- weapons

export const WeaponStatsSchema = z.object({
  fireRate: z.number(),
  magazineSize: z.number(),
  runSpeedMultiplier: z.float32(),
  equipTimeSeconds: z.float32(),
  reloadTimeSeconds: z.float32(),
  firstBulletAccuracy: z.float32(),
  shotgunPelletCount: z.number(),
  wallPenetration: z.string(),
  feature: z.nullable(z.string()),
  fireMode: z.nullable(z.string()),
  altFireType: z.nullable(z.string()),
  adsStats: z.nullable(z.json()),
  altShotgunStats: z.nullable(z.json()),
  airBurstStats: z.nullable(z.json()),
  damageRanges: z.json()
});
export type WeaponStats = z.infer<typeof WeaponStatsSchema>;

export const WeaponShopDataSchema = z.object({
  cost: z.number(),
  category: z.string(),
  shopOrderPriority: z.number(),
  categoryText: z.json(),
  gridPosition: z.nullable(z.json()),
  canBeTrashed: z.boolean(),
  image: z.nullable(z.url()),
  newImage: z.nullable(z.url()),
  newImage2: z.nullable(z.url()),
});
export type WeaponShopData = z.infer<typeof WeaponShopDataSchema>;

export const WeaponSkinChromaSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  displayIcon: z.nullable(z.url()),
  fullRender: z.url(),
  swatch: z.nullable(z.url()),
  streamedVideo: z.nullable(z.url())
});
export type WeaponSkiChroma = z.infer<typeof WeaponSkinChromaSchema>;

export const WeaponSkinLevelSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  displayIcon: z.nullable(z.url()),
  streamedVideo: z.nullable(z.url())
});
export type WeaponSkinLevel = z.infer<typeof WeaponSkinLevelSchema>;

export const WeaponSkinSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  themeUuid: z.guid(),
  contentTierUuid: z.nullable(z.guid()),
  displayIcon: z.nullable(z.url()),
  wallpaper: z.nullable(z.url()),
  chromas: z.array(WeaponSkinChromaSchema),
  levels: z.array(WeaponSkinLevelSchema)
});
export type WeaponSkin = z.infer<typeof WeaponSkinSchema>;

export const WeaponSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  category: z.string(),
  defaultSkinUuid: z.guid(),
  displayIcon: z.url(),
  killStreamIcon: z.url(),
  weaponStats: z.nullable(WeaponStatsSchema),
  shopData: z.nullable(WeaponShopDataSchema),
  skins: z.array(WeaponSkinSchema),
});
export type Weapon = z.infer<typeof WeaponSchema>;

// endregion: -- weapons
// region:    -- contracts

export const ContractLevelRewardSchema = z.object({
  type: z.string(),
  uuid: z.guid(),
  amount: z.number()
});
export type ContractLevelReward = z.infer<typeof ContractLevelRewardSchema>;

export const ContractLevelSchema = z.object({
  xp: z.number(),
  isPurchasableWithVP: z.boolean(),
  isPurchasableWithDough: z.boolean(),
  vpCost: z.number(),
  doughCost: z.number(),
  reward: ContractLevelRewardSchema
});
export type ContractLevel = z.infer<typeof ContractLevelSchema>;

export const ContractSchema = z.object({
  uuid: z.guid(),
  displayName: z.json(),
  displayIcon: z.nullable(z.url()),
  content: z.object({
    relationType: z.nullable(z.string()),
    relationUuid: z.nullable(z.guid()),
    premiumVPCost: z.number(),
    chapters: z.array(z.object({
      isEpilogue: z.boolean(),
      levels: z.array(ContractLevelSchema),
      freeRewards: z.nullable(z.array(ContractLevelRewardSchema))
    }))
  })
});
export type Contract = z.infer<typeof ContractSchema>;

// endregion: -- contracts
