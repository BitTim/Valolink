/*
 * Copyright (c) 2026 Tim Anhalt (BitTim)
 *
 * Project:    Valolink
 * License:    GPLv3
 *
 * File:       vite.config.ts
 * Module:     Valolink
 * Author:     Tim Anhalt (BitTim)
 * Modified:   31.03.26, 03:15
 */

import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
})
