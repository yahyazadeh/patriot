if sensor_value('g1/ts01') == 0:
    if lock('pdp_lock', timeout=3, expires=2):
        if pdp('g1/drain_valve', 1, None):
                action('g1/drain_valve', status=1)

        unlock('pdp_lock')

