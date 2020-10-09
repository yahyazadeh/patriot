if lock('pdp_lock', timeout=3, expires=2):
    if pdp('g1/mixing_robot', 1, None):
        action('g1/mixing_robot', status=1)

    unlock('pdp_lock')

