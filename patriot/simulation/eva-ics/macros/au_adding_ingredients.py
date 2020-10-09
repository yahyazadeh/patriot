if sensor_value('g1/lli01') == 0:    
    action('g1/lifting_arm', status=1)
    action('g1/compound_valve01', status=1)
    action('g1/compound_valve02', status=1)
