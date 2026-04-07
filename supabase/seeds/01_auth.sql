-- Sourced from https://pastebin.com/pxf253Up

-- Create a function to add a test user
CREATE OR REPLACE FUNCTION add_test_user(
  p_email TEXT,
  p_user_id UUID,
  p_password TEXT, -- Plain password, will be hashed inside function
  p_display_name TEXT,
  p_is_active BOOLEAN DEFAULT TRUE
) 
RETURNS UUID 
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = ''
AS $$
DECLARE
  v_user_id UUID;
BEGIN
  -- 1. Create the user in auth.users
  INSERT INTO auth.users (
    instance_id,
    id,
    aud,
    role,
    email,
    encrypted_password,
    email_confirmed_at,
    raw_app_meta_data,
    raw_user_meta_data,
    created_at,
    updated_at,
    -- confirmed_at, -- SQLERRM: cannot insert a non-DEFAULT value into column "confirmed_at"
    is_sso_user,
    is_anonymous,
    confirmation_token,
    recovery_token,
    email_change_token_new,
    email_change,
    phone_change,
    phone_change_token
  )
  VALUES (
    '00000000-0000-0000-0000-000000000000',
    p_user_id,
    'authenticated',
    'authenticated',
    p_email,
    extensions.crypt(p_password, extensions.gen_salt('bf')), -- Hash the password
    now(),
    '{"provider":"email","providers":["email"]}',
    '{"email_verified":true}',
    now(),
    now(),
    -- now(),     -- confirmed_at, -- SQLERRM: cannot insert a non-DEFAULT value into column "confirmed_at"
    false,
    false,
    '', -- confirmation_token
    '', -- recovery_token
    '', -- email_change_token_new
    '', -- email_change
    '', -- phone_change
    '' -- phone_change_token

  ) RETURNING id INTO STRICT v_user_id;

  -- 2. Create the corresponding identity in auth.identities
  INSERT INTO auth.identities (
    id,
    user_id,
    provider_id,
    provider,
    identity_data,
    last_sign_in_at,
    created_at,
    updated_at
    --email generated column
  )
  VALUES (
    gen_random_uuid(),
    v_user_id,
    v_user_id::text, -- Use user_id as the provider_id
    'email',
    format('{"sub":"%s","email":"%s", "email_verified":false,"phone_verified":false}', v_user_id::text, p_email)::jsonb,
    now(),
    now(),
    now()
    -- p_email
  );
  
  RETURN v_user_id;
END;
$$;

REVOKE EXECUTE ON FUNCTION add_test_user(TEXT, UUID, TEXT,  TEXT, BOOLEAN) FROM public;
GRANT EXECUTE ON FUNCTION add_test_user(TEXT, UUID, TEXT,  TEXT, BOOLEAN) TO postgres; -- Or specific admin roles

-- Now use the function to create multiple test users
DO $$
DECLARE
  test_password TEXT := 'test123'; -- Shared for all test users
  alice_id UUID;
  bob_id UUID;
  carol_id UUID;
  dave_id UUID;
  erin_id UUID;
  fred_id UUID;
  grace_id UUID;
  hans_id UUID;
BEGIN
  -- Only run in development environment
  IF current_database() = 'postgres' THEN
    RAISE NOTICE 'Detected local development database (%). Running seed script to create test users.', current_database();

    -- Create users
    alice_id := add_test_user(
      'alice@example.com', 
      '00000000-0000-0000-0000-000000000001'::UUID, -- Use a fixed UUID for admin
      test_password,
      'Alice'
    );
    RAISE NOTICE 'Created Alice with id %, pw `%`', alice_id, test_password;

    bob_id := add_test_user(
      'bob@example.com',
      '00000000-0000-0000-0000-000000000002'::UUID, -- Use a fixed UUID for support1
      test_password,
      'Bob'
    );
    RAISE NOTICE 'Created Bob with id %', bob_id;
    
    carol_id := add_test_user(
      'carol@example.com',
      '00000000-0000-0000-0000-000000000003'::UUID, -- Use a fixed UUID for support2
      test_password,
      'Carol'
    );
    RAISE NOTICE 'Created Carol with id %', carol_id;
    
    dave_id := add_test_user(
      'dave@example.com',
      '00000000-0000-0000-0000-000000000004'::UUID, -- Use a fixed UUID for inactive user
      test_password,
      'Dave'
    );
    RAISE NOTICE 'Created Dave with id %', dave_id;
    
    erin_id := add_test_user(
      'erin@example.com',
      '00000000-0000-0000-0000-000000000005'::UUID, -- Use a fixed UUID for inactive user
      test_password,
      'Erin'
    );
    RAISE NOTICE 'Created Erin with id %', erin_id;
    
    fred_id := add_test_user(
      'fred@example.com',
      '00000000-0000-0000-0000-000000000006'::UUID, -- Use a fixed UUID for inactive user
      test_password,
      'Fred'
    );
    RAISE NOTICE 'Created Fred with id %', fred_id;

    grace_id := add_test_user(
      'grace@example.com',
      '00000000-0000-0000-0000-000000000007'::UUID, -- Use a fixed UUID for inactive user
      test_password,
      'Grace'
    );
    RAISE NOTICE 'Created Grace with id %', grace_id;

    hans_id := add_test_user(
      'hans@example.com',
      '00000000-0000-0000-0000-000000000008'::UUID, -- Use a fixed UUID for inactive user
      test_password,
      'Hans'
    );
    RAISE NOTICE 'Created Hans with id %', hans_id;

    -- Add additional seed data here (e.g., carees, care teams)
    -- You could create additional functions for those if needed
    
    RAISE NOTICE 'Test user seeding completed successfully.';
  ELSE
    RAISE NOTICE 'Skipping test user seeding. Database (%) is not the expected local development database.', current_database();
  END IF;
EXCEPTION
  WHEN OTHERS THEN
    RAISE WARNING 'An error occurred during seed script execution:';
    RAISE WARNING 'SQLERRM: %', SQLERRM;
    RAISE WARNING 'SQLSTATE: %', SQLSTATE;
END $$;

-- Drop the function after use (optional)
-- DROP FUNCTION IF EXISTS add_test_user(TEXT, UUID, TEXT, TEXT, BOOLEAN);

