#!/usr/bin/python3
from bchmemo import MemoUser

#lubokkanev
user=MemoUser('bitcoincash:qp4ek7dm8g8k84j96j5ye0wdrrxc7pcm5qhnmwpy29')
user.private_key = 'KyPu4vkM96pTnYgQuwmXAzwpLhX2haYuJrx9zwsXi5LZz3PWBdZS'

print(user.get_post_memo_signed_transaction("1234567891123456789212345678931234567894123456789512345678961234567897123456789812345678991234567890")) # 100 characters long

