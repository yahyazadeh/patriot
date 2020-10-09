if lock('pdp_lock', timeout=3, expires=2):
    if pdp('g1/stopper', 0, None):
        action('g1/stopper', status=0)

    unlock('pdp_lock')

