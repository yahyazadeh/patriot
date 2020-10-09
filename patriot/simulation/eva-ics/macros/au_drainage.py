if sensor_value('g1/ts01') == 0:
    action('g1/drain_valve', status=1)
