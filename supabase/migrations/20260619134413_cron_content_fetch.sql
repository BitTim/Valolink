create extension if not exists pg_cron;

select cron.schedule(
    'sync-content',
    '0 0,6,12,18 * * *',
    $$
    select net.http_post(
        url := 'https://dlyaguloerboafnlvzhe.supabase.co/functions/v1/sync-content',
        headers := '{}'::jsonb,
        body := '{ "name": "Functions" }',
        timeout_milliseconds := 1000
    )
    $$
);