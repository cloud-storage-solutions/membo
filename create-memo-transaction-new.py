#!/usr/bin/python3
from bchmemo import MemoUser

#lubokkanev - new 
user=MemoUser('bitcoincash:qrjuajjkgm03yshp8l9c7m5qe0nlt9qvz54jry37ej')
user.private_key = 'Kzw6hXr8VBKeLXJUV7LRHTFZuhX2duWsX4gEBzt2W2Yme2zk84bR'

#print(user.get_post_memo_signed_transaction("1234567891123456789212345678931234567894123456789512345678961234567897123456789812345678991234567890")) # 100 characters long
user.post_memo("012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789") # 150 
user.get_memos()
