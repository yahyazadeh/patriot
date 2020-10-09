if sensor_value('g1/lli01') == 0:    
    if lock('pdp_lock', timeout=3, expires=2):
        if pdp('g1/lifting_arm', 1, None):
                action('g1/lifting_arm', status=1)

        unlock('pdp_lock')

    if lock('pdp_lock', timeout=3, expires=2):
        if pdp('g1/compound_valve01', 1, None):
                action('g1/compound_valve01', status=1)

        unlock('pdp_lock')

    if lock('pdp_lock', timeout=3, expires=2):
        if pdp('g1/compound_valve02', 1, None):
                action('g1/compound_valve02', status=1)

        unlock('pdp_lock')

