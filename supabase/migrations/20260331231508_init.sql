create table users (
    id uuid not null default auth.uid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    username text not null default '',
    avatar text,
    is_private boolean not null default false,

    constraint users_pkey primary key (id),
    constraint users_id_fkey foreign key (id) references auth.users(id)
);

create table flags (
    uid uuid not null default auth.uid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    has_onboarded boolean not null default false,
    has_rank boolean not null default false,

    constraint flags_pkey primary key (uid),
    constraint flags_uid_fkey foreign key (uid) references users(id)
);

create table follows (
    follower uuid not null default auth.uid(),
    following uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    accepted boolean not null default false,

    constraint follows_pkey primary key (follower, following),
    constraint follows_no_self check (follower != following),
    constraint follows_follower_fkey foreign key (follower) references users(id),
    constraint follows_following_fkey foreign key (following) references users(id)
);

create table agents (
    uid uuid not null default auth.uid(),
    agent uuid not null,
    created_at timestamp with time zone not null default now(),

    constraint agents_pkey primary key (uid, agent),
    constraint agents_uid_fkey foreign key (uid) references users(id)
);

create table contracts (
    uid uuid not null default auth.uid(),
    contract uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    free_only boolean not null default false,
    xp_offset integer not null default 0,

    constraint contracts_pkey primary key (uid, contract),
    constraint contracts_uid_fkey foreign key (uid) references users(id)
);

create table levels (
    uid uuid not null default auth.uid(),
    contract uuid not null,
    level uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    is_purchased boolean not null default false,

    constraint levels_pkey primary key (uid, contract, level),
    constraint levels_uid_contract_fkey foreign key (uid, contract) references contracts(uid, contract)
);

create table match_details (
    id uuid not null default gen_random_uuid(),
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    score_a integer not null default 0 check (score_a >= 0),
    score_b integer check (score_b >= 0),
    end_reason text not null default 'COMPLETED' check (end_reason in ('COMPLETED', 'SURRENDER_A', 'SURRENDER_B')),
    time timestamp with time zone not null default now(),
    map uuid not null,
    mode uuid not null,

    constraint match_details_pkey primary key (id)
);

create table matches (
    uid uuid not null default auth.uid(),
    details uuid not null,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now(),
    xp integer,
    rr integer,
    rr_offset integer,
    is_owner boolean not null default false,
    is_team_b boolean not null default false,

    constraint matches_pkey primary key (uid, details),
    constraint matches_uid_fkey foreign key (uid) references users(id),
    constraint matches_details_fkey foreign key (details) references match_details(id)
);

create unique index matches_one_owner_per_detail
on matches (details)
where is_owner = true;

create table rel_match_contract (
    uid uuid not null default auth.uid(),
    contract uuid not null,
    details uuid not null,
    created_at timestamp with time zone not null default now(),

    constraint rel_match_contract_pkey primary key (uid, contract, details),
    constraint rel_match_contract_uid_contract_fkey foreign key (uid, contract) references contracts(uid, contract),
    constraint rel_match_contract_uid_details_fkey foreign key (uid, details) references matches(uid, details)
);