import requests
import json
import time
import urllib3


BASE_URL = "https://localhost:8888"
LOCK_NAME = "PatriotLock"

urllib3.disable_warnings(urllib3.exceptions.SecurityWarning)

lock_dict = {"title": "client", "lifetime": 1000, "wait": 20}
raw_body = json.dumps(lock_dict)

def acquire_lock():
    return requests.post("%s/locks/%s" % (BASE_URL, LOCK_NAME), data=raw_body,
                  verify='/Users/daniel/research_project/IoT/patriot-rdlm/restful-distributed-lock-manager/keys/server.crt',
                  auth=('admin', 'P@Tr!0t'))

r = acquire_lock()
print('\nacquire lock status code: {}\n'.format(r.status_code))
while r.status_code != 201:
    r = acquire_lock()
    print('wait for lock.\n')
print('lock is acquired!\n')
print('**Enter critical section**\n')
lock_url = r.headers['Location']
print('lock url: {}\n'.format(lock_url))

for i in range(1, 21):
    time.sleep(1)
    print('i: {}'.format(i))

r = requests.delete(lock_url, verify='/Users/daniel/research_project/IoT/patriot-rdlm/restful-distributed-lock-manager/keys/server.crt',
                    auth=('admin', 'P@Tr!0t'))
print('\nrelease status code: {}\n'.format(r.status_code))
if r.status_code == 204:
    print('lock is released!\n')
else:
    print('lock does not exist anymore!\n')

