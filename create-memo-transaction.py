#!/usr/bin/python3
from bchmemo import MemoUser

#lubokkanev
user=MemoUser('bitcoincash:qp4ek7dm8g8k84j96j5ye0wdrrxc7pcm5qhnmwpy29')
user.private_key = 'KyPu4vkM96pTnYgQuwmXAzwpLhX2haYuJrx9zwsXi5LZz3PWBdZS'

#user.post_memo('Testing something')

print(user.get_post_memo_signed_transaction("testing someting"))

