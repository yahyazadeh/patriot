if lock('pdp_lock', timeout=3, expires=2):
    if pdp('g1/stopper', 1, None):
        action('g1/stopper', status=1)

    unlock('pdp_lock')

