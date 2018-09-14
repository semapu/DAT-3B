#!usr/bin/expect
#			REMOVE LOCAL FOLDERS
spawn rm -r public_html
expect "$ "
spawn rm -r project-schema
expect "$ "
#			LOGIN SSH 
set HOST "soft0.upc.edu"
set USER "ldatusr12"
set PASSWORD "8h3w0fnz"
spawn ssh -p 6767 $USER@$HOST
expect "password:"
send "$PASSWORD\r"
expect "$ "
#			DELETE OLD ZIP FILES FROM THE REMOTE HOST
send "ls |grep \"public_html\\|project-schema\" \r"
expect "$ "
send "rm public_html.zip \r"
expect "$ "
send "rm project-schema.zip \r"
expect "$ "
send "clear \r"
expect "$ "
#			CREATE NEW UPDATES ZIP FILES IN THE REMOTE HOST
send "zip -r public_html.zip public_html \r"
expect "$ "
send "zip -r project-schema.zip project-schema \r"
expect "$ "
send "clear \r"
expect "$ "
#			EXIT SSH in order to copy files through SSH
send "exit \r"
#			COPYING FILES TO LOCAL HOST
spawn scp -P 6767 ldatusr12@soft0.upc.edu:public_html.zip /home/sergi/Documents/3B/Dat/P2
expect "password:"
send "$PASSWORD\r"
expect "$ "
spawn scp -P 6767 ldatusr12@soft0.upc.edu:project-schema.zip /home/sergi/Documents/3B/Dat/P2
expect "password:"
send "$PASSWORD\r"
expect "$ "
#			LOGIN SSH FOR DELETE NEW ZIP FILES FROM REMOTE HOST
set HOST "soft0.upc.edu"
set USER "ldatusr12"
set PASSWORD "8h3w0fnz"
spawn ssh -p 6767 $USER@$HOST
expect "password:"
send "$PASSWORD\r"
expect "$ "
#			DELETE NEW ZIP FILES FROM THE REMOTE HOST
send "ls |grep \"public_html\\|project-schema\" \r"
expect "$ "
send "rm public_html.zip \r"
expect "$ "
send "rm project-schema.zip \r"
expect "$ "
#			EXIT SSH in order to copy files through SSH
send "exit \r"
#			EXTRACT AND DELETE LOCAL ZIP FILES 
spawn unzip public_html.zip
expect "$ "
spawn unzip project-schema.zip
expect "$ "
spawn rm public_html.zip 
expect "$ "
spawn rm project-schema.zip
expect "$ "
spawn clear
expect "$ "
