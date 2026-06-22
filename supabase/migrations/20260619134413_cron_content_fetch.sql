create extension if not exists pg_cron;

-- The Authorization header uses the ANON_KEY which is safe to include in here
select cron.schedule(
    'sync-content',
    '0 0,6,12,18 * * *',
    $$
    select net.http_post(
        url := 'https://dlyaguloerboafnlvzhe.supabase.co/functions/v1/sync-content',
        headers := jsonb_build_object(
            'Content-Type', 'application/json',
            'Authorization', 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImRseWFndWxvZXJib2Fmbmx2emhlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk2MjE0NzMsImV4cCI6MjA5NTE5NzQ3M30.38zXGHt0zVaRvX0ljkLdpWNORumVE5Gt1NJ6NBWDWf0'
        ),
        body := '{ "name": "Functions" }',
        timeout_milliseconds := 1000
    )
    $$
);