#!usr/bin/expect
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
send "cd project-schema\r"
expect "$ "
send "chmod +x ./bin/compile.sh\r"
expect "$ "
send "./bin/compile.sh\r"
expect "$ "
send "cp build/bin/mail.cgi ../public_html/practica2  \r"
expect "$ "
#			EXIT SSH 
send "exit \r"
expect "$ "
