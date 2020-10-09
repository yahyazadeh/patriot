if lock('pdp_lock', timeout=3, expires=2):
    if pdp('g1/water_valve', 1, None):
        action('g1/water_valve', status=1)

    unlock('pdp_lock')

