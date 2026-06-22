import * as z from "@zod/zod";
import { ApiResponseSchema } from "./schemas.ts";

export async function apiFetch<T>(
	url: string,
	schema: z.ZodType<T>,
	options?: RequestInit
): Promise<T> {
	const res = await fetch(url, options);
	const json = await res.json();

	const envelope = ApiResponseSchema(schema).parse(json);

	if ('error' in envelope) {
		throw new Error(`API error ${envelope.status}: ${envelope.error}`);
	}

	return envelope.data;
}

export async function safeApiFetch<T>(
	url: string,
	schema: z.ZodType<T>,
	options?: RequestInit
): Promise<{ data: T; error: null } | { data: null; error: string }> {
	try {
		const data = await apiFetch(url, schema, options);
		return { data, error: null };
	} catch (e) {
		return { data: null, error: (e as Error).message };
	}
}