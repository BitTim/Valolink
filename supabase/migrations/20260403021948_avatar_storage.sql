insert into storage.buckets (id, name, public)
values ('avatars', 'avatars', false);

create policy "Authenticated users can read all avatars"
on storage.objects for select
to authenticated
using (bucket_id = 'avatars');

create policy "Users can insert own avatar"
on storage.objects for insert
to authenticated
with check (
    bucket_id = 'avatars'
    and (storage.foldername(name))[1] = (select auth.uid())::text
);

create policy "Users can update own avatar"
on storage.objects for update
to authenticated
using (
    bucket_id = 'avatars'
    and (storage.foldername(name))[1] = (select auth.uid())::text
);

create policy "Users can delete own avatar"
on storage.objects for delete
to authenticated
using (
    bucket_id = 'avatars'
    and (storage.foldername(name))[1] = (select auth.uid())::text
);