#!usr/bin/expect
#			ZIP THE FOLDERS TO UPLOAD
spawn zip -r public_html.zip public_html
expect "$ "
spawn zip -r project-schema.zip project-schema
expect "$ "
#			LOGIN SSH 
set HOST "soft0.upc.edu"
set USER "ldatusr12"
set PASSWORD "8h3w0fnz"
spawn ssh -p 6767 $USER@$HOST
expect "password:"
send "$PASSWORD\r"
expect "$ "
send "clear \r"
expect "$ "
#			DELETE OLD FOLDERS
send "rm -r public_html \r"
expect "$ "
send "rm -r project-schema \r"
expect "$ "
#			EXIT SSH 
send "exit \r"
expect "$ "
#			COPYING FILES 
spawn scp -P 6767 /home/sergi/Documents/3B/Dat/P2/public_html.zip ldatusr12@soft0.upc.edu:
expect "password:"
send "$PASSWORD\r"
expect "$ "
spawn scp -P 6767 /home/sergi/Documents/3B/Dat/P2/project-schema.zip ldatusr12@soft0.upc.edu:
expect "password:"
send "$PASSWORD\r"
expect "$ "
#			LOGIN SSH in order to extract zip files
spawn ssh -p 6767 $USER@$HOST
expect "password:"
send "$PASSWORD\r"
expect "$ "
send "clear \r"
expect "$ "
#			EXTRACT AND REMOVE ZIP FILES FROM THE REMOTE HOST
send "unzip public_html.zip \r"
expect "$ "
send "unzip project-schema.zip \r"
expect "$ "
send "clear \r"
expect "$ "
send "rm -r public_html.zip \r"
expect "$ "
send "rm -r project-schema.zip \r"
expect "$ "
send "ls |grep \"public_html\\|project-schema\" \r"
expect "$ "
#send "clear \r"
#expect "$ "
#			EXIT SSH in order to copy fies through SSH
send "exit \r"
expect "$ "
#			REMOVE LOCAL ZIP FILES CREATED in THE BEGINNING
spawn rm -r public_html.zip 
expect "$ "
spawn rm -r project-schema.zip 
expect "$ "
