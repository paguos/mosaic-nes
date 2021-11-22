import os

from pathlib import Path

questions = [
        {
            'type': 'list',
            'name': 'experiment',
            'message': 'Experiment name:',
            'choices': [
                'variable_update_intervals',
                'variable_coordinator_update_interval',
                'variable_source_update_interval',
                'max_speed_sink',
                'max_speed_source',
                'moving_range',
                'rsu_range',
                'range_query_interval',
                'filter_storage'
            ],
        },
        {
            'type': 'list',
            'name': 'scenario',
            'message': 'Scenario:',
            'choices': [
                'barcelona', 'barcelona-moving-range', 'barcelona-range-query', 
                'berlin', 'berlin-moving-range', 'berlin-range-query',
                'leipzig', 'leipzig-moving-range', 'leipzig-duplicates', 'leipzig-duplicates-moving-range'
                ],
        },
        {
            'type': 'input',
            'name': 'brake',
            'message': 'Brake:',
            'default': '1'
        },
        {
            'type': 'input',
            'name': 'config_start',
            'message': 'Start value:',
            'default': '0'
        },
        {
            'type': 'input',
            'name': 'config_interval',
            'message': 'Interval value:',
            'default': '500'
        },
        {
            'type': 'input',
            'name': 'config_repetitions',
            'message': 'Number of repetitions:',
            'default': '5'
        },
        {
            'type': 'input',
            'name': 'mosaic_path',
            'message': 'Mosaic path:',
            'default': f"{Path(os.getcwd()).parent.parent}/mosaic"
        }
    ]