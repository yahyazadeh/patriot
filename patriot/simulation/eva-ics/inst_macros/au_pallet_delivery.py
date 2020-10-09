if lock('pdp_lock', timeout=3, expires=2):
    if pdp('g1/lifter_robot', 1, None):
        action('g1/lifter_robot', status=1)

    unlock('pdp_lock')

