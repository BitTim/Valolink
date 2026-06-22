export type Json =
  | string
  | number
  | boolean
  | null
  | { [key: string]: Json | undefined }
  | Json[]

export type Database = {
  graphql_public: {
    Tables: {
      [_ in never]: never
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      graphql: {
        Args: {
          extensions?: Json
          operationName?: string
          query?: string
          variables?: Json
        }
        Returns: Json
      }
    }
    Enums: {
      [_ in never]: never
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
  public: {
    Tables: {
      activities: {
        Row: {
          created_at: string
          id: string
          rr: number | null
          time: string
          type: string
          updated_at: string
          user_id: string
          xp: number
        }
        Insert: {
          created_at?: string
          id?: string
          rr?: number | null
          time?: string
          type: string
          updated_at?: string
          user_id?: string
          xp?: number
        }
        Update: {
          created_at?: string
          id?: string
          rr?: number | null
          time?: string
          type?: string
          updated_at?: string
          user_id?: string
          xp?: number
        }
        Relationships: [
          {
            foreignKeyName: "activities_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      agents: {
        Row: {
          agent: string
          created_at: string
          end_time: string | null
          owned_state: string
          start_time: string | null
          total_xp: number
          updated_at: string
          user_id: string
          xp_offset: number
        }
        Insert: {
          agent: string
          created_at?: string
          end_time?: string | null
          owned_state?: string
          start_time?: string | null
          total_xp?: number
          updated_at?: string
          user_id?: string
          xp_offset?: number
        }
        Update: {
          agent?: string
          created_at?: string
          end_time?: string | null
          owned_state?: string
          start_time?: string | null
          total_xp?: number
          updated_at?: string
          user_id?: string
          xp_offset?: number
        }
        Relationships: [
          {
            foreignKeyName: "agents_agent_fkey"
            columns: ["agent"]
            isOneToOne: false
            referencedRelation: "valo_agents"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "agents_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      flags: {
        Row: {
          created_at: string
          has_onboarded: boolean
          updated_at: string
          user_id: string
        }
        Insert: {
          created_at?: string
          has_onboarded?: boolean
          updated_at?: string
          user_id?: string
        }
        Update: {
          created_at?: string
          has_onboarded?: boolean
          updated_at?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "flags_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: true
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      follows: {
        Row: {
          created_at: string
          follower: string
          following: string
          relation_status: string
          updated_at: string
        }
        Insert: {
          created_at?: string
          follower?: string
          following: string
          relation_status?: string
          updated_at?: string
        }
        Update: {
          created_at?: string
          follower?: string
          following?: string
          relation_status?: string
          updated_at?: string
        }
        Relationships: [
          {
            foreignKeyName: "follows_follower_fkey"
            columns: ["follower"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
          {
            foreignKeyName: "follows_following_fkey"
            columns: ["following"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      match_participants: {
        Row: {
          activity: string
          created_at: string
          is_owner: boolean
          is_team_b: boolean
          match: string
          updated_at: string
          user_id: string
        }
        Insert: {
          activity: string
          created_at?: string
          is_owner?: boolean
          is_team_b?: boolean
          match: string
          updated_at?: string
          user_id?: string
        }
        Update: {
          activity?: string
          created_at?: string
          is_owner?: boolean
          is_team_b?: boolean
          match?: string
          updated_at?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "match_participants_activity_fkey"
            columns: ["user_id", "activity"]
            isOneToOne: true
            referencedRelation: "activities"
            referencedColumns: ["user_id", "id"]
          },
          {
            foreignKeyName: "match_participants_match_fkey"
            columns: ["match"]
            isOneToOne: false
            referencedRelation: "matches"
            referencedColumns: ["id"]
          },
        ]
      }
      matches: {
        Row: {
          created_at: string
          end_reason: string
          id: string
          is_ranked: boolean
          map: string
          mode: string
          score_a: number
          score_b: number | null
          updated_at: string
        }
        Insert: {
          created_at?: string
          end_reason?: string
          id?: string
          is_ranked?: boolean
          map: string
          mode: string
          score_a?: number
          score_b?: number | null
          updated_at?: string
        }
        Update: {
          created_at?: string
          end_reason?: string
          id?: string
          is_ranked?: boolean
          map?: string
          mode?: string
          score_a?: number
          score_b?: number | null
          updated_at?: string
        }
        Relationships: [
          {
            foreignKeyName: "matches_map_fkey"
            columns: ["map"]
            isOneToOne: false
            referencedRelation: "valo_maps"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "matches_mode_fkey"
            columns: ["mode"]
            isOneToOne: false
            referencedRelation: "valo_modes"
            referencedColumns: ["uuid"]
          },
        ]
      }
      progressions: {
        Row: {
          created_at: string
          end_time: string | null
          free_only: boolean
          progression: string
          start_time: string | null
          total_xp: number
          track_xp: boolean
          updated_at: string
          user_id: string
          xp_offset: number
        }
        Insert: {
          created_at?: string
          end_time?: string | null
          free_only?: boolean
          progression: string
          start_time?: string | null
          total_xp?: number
          track_xp?: boolean
          updated_at?: string
          user_id?: string
          xp_offset?: number
        }
        Update: {
          created_at?: string
          end_time?: string | null
          free_only?: boolean
          progression?: string
          start_time?: string | null
          total_xp?: number
          track_xp?: boolean
          updated_at?: string
          user_id?: string
          xp_offset?: number
        }
        Relationships: [
          {
            foreignKeyName: "progressions_progression_fkey"
            columns: ["progression"]
            isOneToOne: false
            referencedRelation: "valo_progressions"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "progressions_user_id_fkey"
            columns: ["user_id"]
            isOneToOne: false
            referencedRelation: "users"
            referencedColumns: ["id"]
          },
        ]
      }
      purchased_levels: {
        Row: {
          created_at: string
          level: string
          progression: string
          user_id: string
        }
        Insert: {
          created_at?: string
          level: string
          progression: string
          user_id?: string
        }
        Update: {
          created_at?: string
          level?: string
          progression?: string
          user_id?: string
        }
        Relationships: [
          {
            foreignKeyName: "purchased_levels_user_id_progression_fkey"
            columns: ["user_id", "progression"]
            isOneToOne: false
            referencedRelation: "progressions"
            referencedColumns: ["user_id", "progression"]
          },
        ]
      }
      users: {
        Row: {
          avatar: string | null
          created_at: string
          id: string
          is_private: boolean
          updated_at: string
          username: string
        }
        Insert: {
          avatar?: string | null
          created_at?: string
          id?: string
          is_private?: boolean
          updated_at?: string
          username?: string
        }
        Update: {
          avatar?: string | null
          created_at?: string
          id?: string
          is_private?: boolean
          updated_at?: string
          username?: string
        }
        Relationships: []
      }
      valo_agent_abilities: {
        Row: {
          agent: string
          description: Json
          display_icon: string | null
          display_name: Json
          slot: string
        }
        Insert: {
          agent: string
          description: Json
          display_icon?: string | null
          display_name: Json
          slot: string
        }
        Update: {
          agent?: string
          description?: Json
          display_icon?: string | null
          display_name?: Json
          slot?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_agent_abilities_agent_fkey"
            columns: ["agent"]
            isOneToOne: false
            referencedRelation: "valo_agents"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_agent_recruitments: {
        Row: {
          agent: string
          end_time: string
          start_time: string
          xp: number
        }
        Insert: {
          agent: string
          end_time: string
          start_time: string
          xp: number
        }
        Update: {
          agent?: string
          end_time?: string
          start_time?: string
          xp?: number
        }
        Relationships: [
          {
            foreignKeyName: "valo_agent_recruitments_agent_fkey"
            columns: ["agent"]
            isOneToOne: true
            referencedRelation: "valo_agents"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_agent_roles: {
        Row: {
          description: Json
          display_icon: string
          display_name: Json
          uuid: string
        }
        Insert: {
          description: Json
          display_icon: string
          display_name: Json
          uuid: string
        }
        Update: {
          description?: Json
          display_icon?: string
          display_name?: Json
          uuid?: string
        }
        Relationships: []
      }
      valo_agents: {
        Row: {
          background: string
          background_gradient_colors: string[]
          bust_portrait: string
          description: Json
          developer_name: string
          display_icon: string
          display_icon_small: string
          display_name: Json
          full_portrait: string
          full_portrait_v2: string
          home_screen_promo_tile_image: string | null
          is_base_content: boolean
          is_full_portrait_right_facing: boolean
          killfeed_portrait: string
          minimap_portrait: string
          release_date: string
          role: string
          uuid: string
        }
        Insert: {
          background: string
          background_gradient_colors: string[]
          bust_portrait: string
          description: Json
          developer_name: string
          display_icon: string
          display_icon_small: string
          display_name: Json
          full_portrait: string
          full_portrait_v2: string
          home_screen_promo_tile_image?: string | null
          is_base_content: boolean
          is_full_portrait_right_facing: boolean
          killfeed_portrait: string
          minimap_portrait: string
          release_date: string
          role: string
          uuid: string
        }
        Update: {
          background?: string
          background_gradient_colors?: string[]
          bust_portrait?: string
          description?: Json
          developer_name?: string
          display_icon?: string
          display_icon_small?: string
          display_name?: Json
          full_portrait?: string
          full_portrait_v2?: string
          home_screen_promo_tile_image?: string | null
          is_base_content?: boolean
          is_full_portrait_right_facing?: boolean
          killfeed_portrait?: string
          minimap_portrait?: string
          release_date?: string
          role?: string
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_agents_role_fkey"
            columns: ["role"]
            isOneToOne: false
            referencedRelation: "valo_agent_roles"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_buddies: {
        Row: {
          charm_level: number
          display_icon: string
          display_name: Json
          hide_if_not_owned: boolean
          parent: string
          theme: string | null
          uuid: string
        }
        Insert: {
          charm_level: number
          display_icon: string
          display_name: Json
          hide_if_not_owned: boolean
          parent: string
          theme?: string | null
          uuid: string
        }
        Update: {
          charm_level?: number
          display_icon?: string
          display_name?: Json
          hide_if_not_owned?: boolean
          parent?: string
          theme?: string | null
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_buddies_theme_fkey"
            columns: ["theme"]
            isOneToOne: false
            referencedRelation: "valo_themes"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_cards: {
        Row: {
          display_icon: string
          display_name: Json
          hide_if_not_owned: boolean
          large_art: string | null
          small_art: string
          theme: string | null
          uuid: string
          wide_art: string
        }
        Insert: {
          display_icon: string
          display_name: Json
          hide_if_not_owned: boolean
          large_art?: string | null
          small_art: string
          theme?: string | null
          uuid: string
          wide_art: string
        }
        Update: {
          display_icon?: string
          display_name?: Json
          hide_if_not_owned?: boolean
          large_art?: string | null
          small_art?: string
          theme?: string | null
          uuid?: string
          wide_art?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_cards_theme_fkey"
            columns: ["theme"]
            isOneToOne: false
            referencedRelation: "valo_themes"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_competitive_season_borders: {
        Row: {
          competitive_season: string
          display_icon: string
          level: number
          small_icon: string | null
          uuid: string
          wins_required: number
        }
        Insert: {
          competitive_season: string
          display_icon: string
          level: number
          small_icon?: string | null
          uuid: string
          wins_required: number
        }
        Update: {
          competitive_season?: string
          display_icon?: string
          level?: number
          small_icon?: string | null
          uuid?: string
          wins_required?: number
        }
        Relationships: [
          {
            foreignKeyName: "valo_competitive_season_borders_competitive_season_fkey"
            columns: ["competitive_season"]
            isOneToOne: false
            referencedRelation: "valo_competitive_seasons"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_competitive_seasons: {
        Row: {
          end_time: string
          rank_table: string
          season: string
          start_time: string
          uuid: string
        }
        Insert: {
          end_time: string
          rank_table: string
          season: string
          start_time: string
          uuid: string
        }
        Update: {
          end_time?: string
          rank_table?: string
          season?: string
          start_time?: string
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_competitive_seasons_rank_table_fkey"
            columns: ["rank_table"]
            isOneToOne: false
            referencedRelation: "valo_rank_tables"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "valo_competitive_seasons_season_fkey"
            columns: ["season"]
            isOneToOne: false
            referencedRelation: "valo_seasons"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_content_tiers: {
        Row: {
          developer_name: string
          display_icon: string
          display_name: Json
          highlight_color: string
          juice_cost: number
          juice_value: number
          rank: number
          uuid: string
        }
        Insert: {
          developer_name: string
          display_icon: string
          display_name: Json
          highlight_color: string
          juice_cost: number
          juice_value: number
          rank: number
          uuid: string
        }
        Update: {
          developer_name?: string
          display_icon?: string
          display_name?: Json
          highlight_color?: string
          juice_cost?: number
          juice_value?: number
          rank?: number
          uuid?: string
        }
        Relationships: []
      }
      valo_currencies: {
        Row: {
          display_icon: string
          display_name: Json
          display_name_singular: Json
          large_icon: string
          reward_preview_icon: string
          uuid: string
        }
        Insert: {
          display_icon: string
          display_name: Json
          display_name_singular: Json
          large_icon: string
          reward_preview_icon: string
          uuid: string
        }
        Update: {
          display_icon?: string
          display_name?: Json
          display_name_singular?: Json
          large_icon?: string
          reward_preview_icon?: string
          uuid?: string
        }
        Relationships: []
      }
      valo_events: {
        Row: {
          display_name: Json | null
          end_time: string
          short_display_name: Json | null
          start_time: string
          uuid: string
        }
        Insert: {
          display_name?: Json | null
          end_time: string
          short_display_name?: Json | null
          start_time: string
          uuid: string
        }
        Update: {
          display_name?: Json | null
          end_time?: string
          short_display_name?: Json | null
          start_time?: string
          uuid?: string
        }
        Relationships: []
      }
      valo_flex: {
        Row: {
          display_icon: string
          display_name: Json
          display_name_all_caps: Json
          uuid: string
        }
        Insert: {
          display_icon: string
          display_name: Json
          display_name_all_caps: Json
          uuid: string
        }
        Update: {
          display_icon?: string
          display_name?: Json
          display_name_all_caps?: Json
          uuid?: string
        }
        Relationships: []
      }
      valo_maps: {
        Row: {
          callouts: Json | null
          category: string
          coordinates: Json | null
          display_icon: string | null
          display_name: Json
          list_view_icon: string
          list_view_icon_tall: string
          premier_background_image: string | null
          splash: string
          stylized_background_image: string | null
          tactical_description: Json | null
          uuid: string
          x_multiplier: number
          x_scalar_to_add: number
          y_multiplier: number
          y_scalar_to_add: number
        }
        Insert: {
          callouts?: Json | null
          category: string
          coordinates?: Json | null
          display_icon?: string | null
          display_name: Json
          list_view_icon: string
          list_view_icon_tall: string
          premier_background_image?: string | null
          splash: string
          stylized_background_image?: string | null
          tactical_description?: Json | null
          uuid: string
          x_multiplier: number
          x_scalar_to_add: number
          y_multiplier: number
          y_scalar_to_add: number
        }
        Update: {
          callouts?: Json | null
          category?: string
          coordinates?: Json | null
          display_icon?: string | null
          display_name?: Json
          list_view_icon?: string
          list_view_icon_tall?: string
          premier_background_image?: string | null
          splash?: string
          stylized_background_image?: string | null
          tactical_description?: Json | null
          uuid?: string
          x_multiplier?: number
          x_scalar_to_add?: number
          y_multiplier?: number
          y_scalar_to_add?: number
        }
        Relationships: []
      }
      valo_modes: {
        Row: {
          can_be_ranked: boolean
          category: string
          description: Json | null
          display_icon: string | null
          display_name: Json
          duration: Json | null
          list_view_icon_tall: string | null
          rounds_per_half: number
          uuid: string
        }
        Insert: {
          can_be_ranked: boolean
          category: string
          description?: Json | null
          display_icon?: string | null
          display_name: Json
          duration?: Json | null
          list_view_icon_tall?: string | null
          rounds_per_half: number
          uuid: string
        }
        Update: {
          can_be_ranked?: boolean
          category?: string
          description?: Json | null
          display_icon?: string | null
          display_name?: Json
          duration?: Json | null
          list_view_icon_tall?: string | null
          rounds_per_half?: number
          uuid?: string
        }
        Relationships: []
      }
      valo_progression_level_rewards: {
        Row: {
          amount: number
          is_free: boolean
          level_index: number
          progression: string
          relation_type: string
          relation_uuid: string
          sort_order: number
        }
        Insert: {
          amount: number
          is_free: boolean
          level_index: number
          progression: string
          relation_type: string
          relation_uuid: string
          sort_order: number
        }
        Update: {
          amount?: number
          is_free?: boolean
          level_index?: number
          progression?: string
          relation_type?: string
          relation_uuid?: string
          sort_order?: number
        }
        Relationships: [
          {
            foreignKeyName: "valo_progression_level_rewards_progress_level_index_fkey"
            columns: ["progression", "level_index"]
            isOneToOne: false
            referencedRelation: "valo_progression_levels"
            referencedColumns: ["progression", "level_index"]
          },
        ]
      }
      valo_progression_levels: {
        Row: {
          is_epilogue: boolean
          is_purchasable_kc: boolean
          is_purchasable_vp: boolean
          kc_cost: number
          level_index: number
          progression: string
          vp_cost: number
          xp: number
        }
        Insert: {
          is_epilogue: boolean
          is_purchasable_kc: boolean
          is_purchasable_vp: boolean
          kc_cost: number
          level_index: number
          progression: string
          vp_cost: number
          xp: number
        }
        Update: {
          is_epilogue?: boolean
          is_purchasable_kc?: boolean
          is_purchasable_vp?: boolean
          kc_cost?: number
          level_index?: number
          progression?: string
          vp_cost?: number
          xp?: number
        }
        Relationships: [
          {
            foreignKeyName: "valo_progression_levels_progression_fkey"
            columns: ["progression"]
            isOneToOne: false
            referencedRelation: "valo_progressions"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_progressions: {
        Row: {
          display_icon: string | null
          display_name: Json
          premium_vp_cost: number
          relation_type: string | null
          relation_uuid: string | null
          uuid: string
        }
        Insert: {
          display_icon?: string | null
          display_name: Json
          premium_vp_cost: number
          relation_type?: string | null
          relation_uuid?: string | null
          uuid: string
        }
        Update: {
          display_icon?: string | null
          display_name?: Json
          premium_vp_cost?: number
          relation_type?: string | null
          relation_uuid?: string | null
          uuid?: string
        }
        Relationships: []
      }
      valo_rank_tables: {
        Row: {
          uuid: string
        }
        Insert: {
          uuid: string
        }
        Update: {
          uuid?: string
        }
        Relationships: []
      }
      valo_ranks: {
        Row: {
          background_color: string
          color: string
          division: string
          division_name: Json
          large_icon: string | null
          rank_table: string
          rank_triangle_down_icon: string | null
          rank_triangle_up_icon: string | null
          small_icon: string | null
          tier: number
          tier_name: Json
        }
        Insert: {
          background_color: string
          color: string
          division: string
          division_name: Json
          large_icon?: string | null
          rank_table: string
          rank_triangle_down_icon?: string | null
          rank_triangle_up_icon?: string | null
          small_icon?: string | null
          tier: number
          tier_name: Json
        }
        Update: {
          background_color?: string
          color?: string
          division?: string
          division_name?: Json
          large_icon?: string | null
          rank_table?: string
          rank_triangle_down_icon?: string | null
          rank_triangle_up_icon?: string | null
          small_icon?: string | null
          tier?: number
          tier_name?: Json
        }
        Relationships: [
          {
            foreignKeyName: "valo_ranks_rank_table_fkey"
            columns: ["rank_table"]
            isOneToOne: false
            referencedRelation: "valo_rank_tables"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_seasons: {
        Row: {
          display_name: Json
          end_time: string
          episode_display_name: Json | null
          start_time: string
          title: Json | null
          uuid: string
        }
        Insert: {
          display_name: Json
          end_time: string
          episode_display_name?: Json | null
          start_time: string
          title?: Json | null
          uuid: string
        }
        Update: {
          display_name?: Json
          end_time?: string
          episode_display_name?: Json | null
          start_time?: string
          title?: Json | null
          uuid?: string
        }
        Relationships: []
      }
      valo_sprays: {
        Row: {
          animation_gif: string | null
          animation_png: string | null
          display_icon: string
          display_name: Json
          full_icon: string | null
          full_transparent_icon: string | null
          hide_if_not_owned: boolean
          theme: string | null
          uuid: string
        }
        Insert: {
          animation_gif?: string | null
          animation_png?: string | null
          display_icon: string
          display_name: Json
          full_icon?: string | null
          full_transparent_icon?: string | null
          hide_if_not_owned: boolean
          theme?: string | null
          uuid: string
        }
        Update: {
          animation_gif?: string | null
          animation_png?: string | null
          display_icon?: string
          display_name?: Json
          full_icon?: string | null
          full_transparent_icon?: string | null
          hide_if_not_owned?: boolean
          theme?: string | null
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_sprays_theme_fkey"
            columns: ["theme"]
            isOneToOne: false
            referencedRelation: "valo_themes"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_themes: {
        Row: {
          display_icon: string | null
          display_name: Json
          store_featured_image: string | null
          uuid: string
        }
        Insert: {
          display_icon?: string | null
          display_name: Json
          store_featured_image?: string | null
          uuid: string
        }
        Update: {
          display_icon?: string | null
          display_name?: Json
          store_featured_image?: string | null
          uuid?: string
        }
        Relationships: []
      }
      valo_titles: {
        Row: {
          display_name: Json | null
          hide_if_not_owned: boolean
          title_text: Json | null
          uuid: string
        }
        Insert: {
          display_name?: Json | null
          hide_if_not_owned: boolean
          title_text?: Json | null
          uuid: string
        }
        Update: {
          display_name?: Json | null
          hide_if_not_owned?: boolean
          title_text?: Json | null
          uuid?: string
        }
        Relationships: []
      }
      valo_version: {
        Row: {
          branch: string
          build_date: string
          build_version: string
          engine_version: string
          id: number
          manifest_id: string
          riot_client_build: string
          riot_client_version: string
          version: string
        }
        Insert: {
          branch: string
          build_date: string
          build_version: string
          engine_version: string
          id?: number
          manifest_id: string
          riot_client_build: string
          riot_client_version: string
          version: string
        }
        Update: {
          branch?: string
          build_date?: string
          build_version?: string
          engine_version?: string
          id?: number
          manifest_id?: string
          riot_client_build?: string
          riot_client_version?: string
          version?: string
        }
        Relationships: []
      }
      valo_weapon_shop_data: {
        Row: {
          can_be_trashed: boolean
          category: string
          category_text: Json
          cost: number
          grid_position: Json | null
          image: string | null
          shop_order_priority: number
          weapon: string
        }
        Insert: {
          can_be_trashed: boolean
          category: string
          category_text: Json
          cost: number
          grid_position?: Json | null
          image?: string | null
          shop_order_priority: number
          weapon: string
        }
        Update: {
          can_be_trashed?: boolean
          category?: string
          category_text?: Json
          cost?: number
          grid_position?: Json | null
          image?: string | null
          shop_order_priority?: number
          weapon?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_weapon_shop_data_weapon_fkey"
            columns: ["weapon"]
            isOneToOne: true
            referencedRelation: "valo_weapons"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_weapon_skin_chromas: {
        Row: {
          display_icon: string | null
          display_name: Json
          full_render: string
          skin: string
          streamed_video: string | null
          swatch: string | null
          uuid: string
        }
        Insert: {
          display_icon?: string | null
          display_name: Json
          full_render: string
          skin: string
          streamed_video?: string | null
          swatch?: string | null
          uuid: string
        }
        Update: {
          display_icon?: string | null
          display_name?: Json
          full_render?: string
          skin?: string
          streamed_video?: string | null
          swatch?: string | null
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_weapon_skin_chromas_skin_fkey"
            columns: ["skin"]
            isOneToOne: false
            referencedRelation: "valo_weapon_skins"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_weapon_skin_levels: {
        Row: {
          display_icon: string | null
          display_name: Json
          skin: string
          streamed_video: string | null
          uuid: string
        }
        Insert: {
          display_icon?: string | null
          display_name: Json
          skin: string
          streamed_video?: string | null
          uuid: string
        }
        Update: {
          display_icon?: string | null
          display_name?: Json
          skin?: string
          streamed_video?: string | null
          uuid?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_weapon_skin_levels_skin_fkey"
            columns: ["skin"]
            isOneToOne: false
            referencedRelation: "valo_weapon_skins"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_weapon_skins: {
        Row: {
          content_tier: string | null
          display_icon: string | null
          display_name: Json
          theme: string
          uuid: string
          wallpaper: string | null
          weapon: string
        }
        Insert: {
          content_tier?: string | null
          display_icon?: string | null
          display_name: Json
          theme: string
          uuid: string
          wallpaper?: string | null
          weapon: string
        }
        Update: {
          content_tier?: string | null
          display_icon?: string | null
          display_name?: Json
          theme?: string
          uuid?: string
          wallpaper?: string | null
          weapon?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_weapon_skins_content_tier_fkey"
            columns: ["content_tier"]
            isOneToOne: false
            referencedRelation: "valo_content_tiers"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "valo_weapon_skins_theme_fkey"
            columns: ["theme"]
            isOneToOne: false
            referencedRelation: "valo_themes"
            referencedColumns: ["uuid"]
          },
          {
            foreignKeyName: "valo_weapon_skins_weapon_fkey"
            columns: ["weapon"]
            isOneToOne: false
            referencedRelation: "valo_weapons"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_weapon_stats: {
        Row: {
          ads_stats: Json | null
          air_burst_stats: Json | null
          alt_fire_type: string | null
          alt_shotgun_stats: Json | null
          damage_ranges: Json
          equip_time_seconds: number
          feature: string | null
          fire_mode: string | null
          fire_rate: number
          first_bullet_accuracy: number
          magazine_size: number
          reload_time_seconds: number
          run_speed_multiplier: number
          shotgun_pellet_count: number
          wall_penetration: string
          weapon: string
        }
        Insert: {
          ads_stats?: Json | null
          air_burst_stats?: Json | null
          alt_fire_type?: string | null
          alt_shotgun_stats?: Json | null
          damage_ranges: Json
          equip_time_seconds: number
          feature?: string | null
          fire_mode?: string | null
          fire_rate: number
          first_bullet_accuracy: number
          magazine_size: number
          reload_time_seconds: number
          run_speed_multiplier: number
          shotgun_pellet_count: number
          wall_penetration: string
          weapon: string
        }
        Update: {
          ads_stats?: Json | null
          air_burst_stats?: Json | null
          alt_fire_type?: string | null
          alt_shotgun_stats?: Json | null
          damage_ranges?: Json
          equip_time_seconds?: number
          feature?: string | null
          fire_mode?: string | null
          fire_rate?: number
          first_bullet_accuracy?: number
          magazine_size?: number
          reload_time_seconds?: number
          run_speed_multiplier?: number
          shotgun_pellet_count?: number
          wall_penetration?: string
          weapon?: string
        }
        Relationships: [
          {
            foreignKeyName: "valo_weapon_stats_weapon_fkey"
            columns: ["weapon"]
            isOneToOne: true
            referencedRelation: "valo_weapons"
            referencedColumns: ["uuid"]
          },
        ]
      }
      valo_weapons: {
        Row: {
          category: string
          default_skin: string
          display_icon: string
          display_name: Json
          kill_stream_icon: string
          uuid: string
        }
        Insert: {
          category: string
          default_skin: string
          display_icon: string
          display_name: Json
          kill_stream_icon: string
          uuid: string
        }
        Update: {
          category?: string
          default_skin?: string
          display_icon?: string
          display_name?: Json
          kill_stream_icon?: string
          uuid?: string
        }
        Relationships: []
      }
    }
    Views: {
      [_ in never]: never
    }
    Functions: {
      check_follow_update: {
        Args: { p_follower: string; p_following: string }
        Returns: boolean
      }
      evaluate_agent_owned_state: {
        Args: {
          p_agent: string
          p_owned_state: string
          p_total_xp: number
          p_xp_offset: number
        }
        Returns: string
      }
      get_follow: {
        Args: { p_follower: string; p_following: string }
        Returns: {
          created_at: string
          follower: string
          following: string
          relation_status: string
          updated_at: string
        }
        SetofOptions: {
          from: "*"
          to: "follows"
          isOneToOne: true
          isSetofReturn: false
        }
      }
      visible_users: { Args: never; Returns: string[] }
    }
    Enums: {
      [_ in never]: never
    }
    CompositeTypes: {
      [_ in never]: never
    }
  }
}

type DatabaseWithoutInternals = Omit<Database, "__InternalSupabase">

type DefaultSchema = DatabaseWithoutInternals[Extract<keyof Database, "public">]

export type Tables<
  DefaultSchemaTableNameOrOptions extends
    | keyof (DefaultSchema["Tables"] & DefaultSchema["Views"])
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
        DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? (DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"] &
      DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Views"])[TableName] extends {
      Row: infer R
    }
    ? R
    : never
  : DefaultSchemaTableNameOrOptions extends keyof (DefaultSchema["Tables"] &
        DefaultSchema["Views"])
    ? (DefaultSchema["Tables"] &
        DefaultSchema["Views"])[DefaultSchemaTableNameOrOptions] extends {
        Row: infer R
      }
      ? R
      : never
    : never

export type TablesInsert<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Insert: infer I
    }
    ? I
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Insert: infer I
      }
      ? I
      : never
    : never

export type TablesUpdate<
  DefaultSchemaTableNameOrOptions extends
    | keyof DefaultSchema["Tables"]
    | { schema: keyof DatabaseWithoutInternals },
  TableName extends DefaultSchemaTableNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"]
    : never = never,
> = DefaultSchemaTableNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaTableNameOrOptions["schema"]]["Tables"][TableName] extends {
      Update: infer U
    }
    ? U
    : never
  : DefaultSchemaTableNameOrOptions extends keyof DefaultSchema["Tables"]
    ? DefaultSchema["Tables"][DefaultSchemaTableNameOrOptions] extends {
        Update: infer U
      }
      ? U
      : never
    : never

export type Enums<
  DefaultSchemaEnumNameOrOptions extends
    | keyof DefaultSchema["Enums"]
    | { schema: keyof DatabaseWithoutInternals },
  EnumName extends DefaultSchemaEnumNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"]
    : never = never,
> = DefaultSchemaEnumNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[DefaultSchemaEnumNameOrOptions["schema"]]["Enums"][EnumName]
  : DefaultSchemaEnumNameOrOptions extends keyof DefaultSchema["Enums"]
    ? DefaultSchema["Enums"][DefaultSchemaEnumNameOrOptions]
    : never

export type CompositeTypes<
  PublicCompositeTypeNameOrOptions extends
    | keyof DefaultSchema["CompositeTypes"]
    | { schema: keyof DatabaseWithoutInternals },
  CompositeTypeName extends PublicCompositeTypeNameOrOptions extends {
    schema: keyof DatabaseWithoutInternals
  }
    ? keyof DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"]
    : never = never,
> = PublicCompositeTypeNameOrOptions extends {
  schema: keyof DatabaseWithoutInternals
}
  ? DatabaseWithoutInternals[PublicCompositeTypeNameOrOptions["schema"]]["CompositeTypes"][CompositeTypeName]
  : PublicCompositeTypeNameOrOptions extends keyof DefaultSchema["CompositeTypes"]
    ? DefaultSchema["CompositeTypes"][PublicCompositeTypeNameOrOptions]
    : never

export const Constants = {
  graphql_public: {
    Enums: {},
  },
  public: {
    Enums: {},
  },
} as const

