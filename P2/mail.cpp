
#include <dat_http/cgi.hpp>
#include <dat_db/ddb.hpp>
#include <iostream>     // cout
#include <dat_http/params.hpp>
#include <string>
#include <map>
#include <dat_http/session.hpp>

using namespace std;
using namespace dat_base;
using namespace dat_http;
using namespace dat_db;

Response mailApp(Request request);

int main(int argc, char **argv, char **env)
{
    cgiApp(env, mailApp);
    return 0;
}


// Nom del directori arrel de la base de dades (relatiu al directori on s'ubica el CGI)
string db_dir = "mail-db";

Response page(Request request);
ByteString ChatPage();
Response error_page(const string& msg);
bool checkLogin (string user, string password);
ByteString loginPage();
string new_seq();
void DeleteMessage(Request request);

  Query info;
  string pathInfo; 
  pair<SessionMap, SaveSession> session; //session guarda la cookie si hay      

  Maybe<string> mb_usuario;
  Maybe<string> mb_contra;
  string cgi_user;
  string cgi_password;

  list<string> ids;

const string CGI_URL = "http://soft0.upc.edu/~ldatusr12/practica2/mail.cgi";

Response mailApp(Request request) //request és el CGI
{
  pathInfo  = request.rawPathInfo(); 
  pair<SessionMap, SaveSession> session = clientLoadSession(request); //session guarda la cookie si hay      

    try {
      if(pathInfo == "/login"){
	    if(request.method() == "GET"){
	      ResponseHeaders hs;
	      hs.push_front(Header("content-Type", MIME_HTML));
	      return Response(ok200, hs, loginPage());
	    }else if (request.method()=="POST"){
	    	Query info = postParams(request);
	    	Maybe<string> mb_button_Login = lookupParam("Login",info);
	    	if(mb_button_Login.isJust() && lookupParam("User", info).isJust() && lookupParam("Password", info).isJust()){
			  	mb_usuario = lookupParam("User", info);
	  			mb_contra = lookupParam("Password", info);
	  			cgi_user = fromJust(mb_usuario);
	  			cgi_password = fromJust(mb_contra);
	       	    if (checkLogin(cgi_user, cgi_password)){	
				    session.first.insert(pair<string,string>("user", cgi_user));  //agafar cookie 
				    ResponseHeaders hs = session.second(session.first);
				    hs.push_back(Header("Content-Type",MIME_HTML));
				    hs.push_front(Header("Location", CGI_URL + "/inbox"));
				    return Response(seeOther303, hs, ChatPage());
	            }else{
		        	return error_page("Validació incorrecte!!");
	         }
           }
	    }
      } else if (pathInfo == "/inbox"){
	  Query info = postParams(request);
	  Maybe<string> mb_button_SendMsg = lookupParam("Send",info);
     	  Maybe<string> mb_button_DeleteMsg = lookupParam("ko",info);
      	  Maybe<string> mb_button_Logout = lookupParam("logOut",info);	        
      	  if(!isNothing(mb_button_SendMsg)){  //detectem que hem enfonsat el botó enviar
	     
			  list<string> aux;
			  DBConnection dbc(db_dir);
	
	  	      time_t tm = time(NULL);
			  string id = new_seq();

			  //REBEM UN CGI I L'HEM DE PASSAR AL MAYBESTRING
			  Maybe<string> mb_to = lookupParam("To", info);
			  Maybe<string> mb_subject = lookupParam("Subject", info);
			  Maybe<string> mb_content = lookupParam("Content", info);
			  	      
			  string to = fromJust(mb_to);
			  string subject = fromJust(mb_subject);
			  string content = fromJust(mb_content);
			  
			  aux.push_back(id); 
			  aux.push_back(session.first["user"]);
			  aux.push_back(to);
			  aux.push_back(to_string((long)tm));
			  aux.push_back(subject);
			  aux.push_back(content);
			  dbc.insert("messages",aux); //afegeix les coses a la bbdd
			  ResponseHeaders hs;
			  hs.push_front(Header("Content-Type",MIME_HTML));
			  return Response(ok200, hs, ChatPage());
		  }else if(!isNothing(mb_button_DeleteMsg)){
		      DeleteMessage(request);
		      session.first.insert(pair<string,string>("user", cgi_user));    
		      ResponseHeaders hs;
			  hs.push_front(Header("Content-Type",MIME_HTML));
			  return Response(ok200, hs, ChatPage());
	    }else if(!isNothing(mb_button_Logout)){
          //refresh cookies
          session.first.erase("user"); 
	      ResponseHeaders hs;
	      hs.push_front(Header("Content-Type",MIME_HTML));
	      return Response(ok200, hs, ChatPage());
        }

      } else {   
	  	  ResponseHeaders hs;
	 	  hs.push_front(Header("Content-Type", "text/plain"));
          return Response(notFound404, hs, "Not Found:" + pathInfo);
      }
       	  ResponseHeaders hs;
          hs.push_front(Header("Content-Type",MIME_HTML));
          return Response(ok200, hs, ChatPage());//el request afegit per nosaltres

    }catch (const LibException& e) {
        return error_page("LibException: " + e.str());
    } catch (const std::exception& e) {
        return error_page(string("Standard exception: ") + e.what());
    } catch (...) {
        return error_page("Unknown exception");
    }
}


/****************************************************************
 * Sortida del CGI
 ****************************************************************/


string new_seq(){
    DBConnection dbc(db_dir);
    dbc.select("msg-seq");
    if (! dbc.next()) {
        throw DBException("No hi ha missatges");
    }
    string pos = dbc.get("value");
    pos = to_string(stoi(pos) + 1);
    dbc.update("msg-seq", "value", pos);
    return pos;
}

Response page(Request request)
{
  
  ByteString html;
 
      html.append("<html><head ><meta charset=\"UTF-8\">\n");
      html.append("<p><b>Assumpte:  <span style=\"color:#2E2EFE\">\n");
      //html.append("<p>").append(to).append(subject).append(content).append("</p>");
      html.append("</span></b></p>\n");
      html.append("</body></html>\n");
  
   ResponseHeaders hs;
   hs.push_back(Header("Content-Type", MIME_HTML));
   return Response(ok200, hs, html);
   
}

ByteString ChatPage(){
    ByteString html;
    DBConnection dbc(db_dir);

    html.append("<html><head><meta charset=\"UTF-8\">\n").
        append("<title >  Servei de missatgeria</title>\n").
        append("</head><body  bgcolor=\"#ffe6f3\">\n").
        append("<center><H1><font color=\"#cc0066\">Benvingut al servei de missatgeria </H1></center></font>\n").
        append("<P><INPUT TYPE=\"submit\" NAME=\"logOut\" VALUE=\"LogOut\"></P>\n").
        append("<table style=\"width:80%\"><tr><td align=\"left\">\n");
	html.append(" <H3>A qui li vols enviar el missatge?</H3>\n").
		append("<FORM METHOD=\"POST\" ACTION=\"#\">"). //# --> Fa que es quedi a inbox.
		append("<P> <INPUT NAME=\"To\" SIZE=\"30\"> </P>\n");
    html.append(" <H3>Introdueixi l'assumpte del missatge:</H3>\n").
		append("<P> <INPUT NAME=\"Subject\" SIZE=\"30\"> </P>\n");
    html.append("<H3>Introdueixi el seu missatge: </H3>\n");
    html.append("<textarea rows=\"4\" cols=\"50\" NAME = \"Content\"></textarea>\n").
		append("<P><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Enviar\"></P>\n").
		append("</FORM>\n").
		append("</td> \n");
    html.append("<td align=\"left\">\n");
  	html.append("<table style=\"width:100%\" >\n");
    
    dbc.select("messages");
	 
    while (dbc.next()) {
      html.append("<input type=\"checkbox\" name=\"Conversa\" value=\"conversa\">\n");
      html.append("<p><b>Assumpte:  <span style=\"color:#2E2EFE\">\n");
      html.append(dbc.get("subject"));
      html.append("</span></b></p>\n");
      html.append("<p><b>Emissor: </b><em>\n");
      html.append(dbc.get("from"));
      html.append("</em></p>");
      html.append("<p><b>Hora: </b><em>\n");
      html.append(dbc.get("time"));
      html.append("</em></p>\n");
      html.append("<p><b>Missatge: </b><br><br>\n");
      html.append(dbc.get("content"));
      html.append("</br></p><hr>\n");
    }
    
    html.append("<P><INPUT TYPE=\"submit\" NAME=\"ko\" VALUE=\"Eliminar\"></P>\n");
    html.append("</table>");
    html.append("</td>\n"). 
		append("</tr></table>\n").
		append("</body></html>\n");
    return html;
}

Response error_page(const string& msg){
    ByteString html;
    html.append("<html><head><meta charset=\"UTF-8\">\n").
        append("<title>Pràctica 2: Mail</title>\n").
        append("</head><body>\n").
        append("<h2>ERROR</h2>").
        append("<h3><font color=\"red\">").append(msg).append("</font></h3>\n").
        append("<p><a href=\"./login\">Pàgina de sessió</a></p>\n").
        append("</body></html>\n");
  
    ResponseHeaders hs;
    hs.push_back(Header("Content-Type", MIME_HTML));
    return Response(internalServerError500, hs, html);
}



bool checkLogin (string user, string password){  
  DBConnection dbc(db_dir);
  dbc.select("users", "name", user);   
  if(dbc.next()){
    string usuari_db = dbc.get("name");
    string pass_db = dbc.get("password");
    if((user.compare(usuari_db) == 0) && (password.compare(pass_db) == 0)){    
      return true;
    }
  }
  return false;
}

ByteString loginPage(){
  ByteString html;
  html.append("<HTML><HEAD><TITLE>Acces</TITLE></TITLE>").
      append("</HEAD><BODY bgcolor=#ffe6f3><center><H1>Accedeixi al seu espai.</H1></center>").
      append("<HR><H3>Introdueixi el nom d'usuari:</H3> ").
      append("<FORM METHOD=\"POST\" ACTION=\"#\"><P> <INPUT NAME=\"user\" SIZE=\"30\"> </P>").
      append("<H3>Introdueixi el Password:</H3>").
      append("<P> <INPUT TYPE= \"Password\" NAME=\"password\" SIZE=\"30\"> </P>").
      append("<P><INPUT TYPE=\"submit\" NAME=\"Login\" VALUE=\"Accedir\"></P></FORM>").
      append("<P>Claudia Ramirez, Sergi Mas</p>").
      append("<HR><HR></BODY></HTML>");
      return html;
}


void DeleteMessage(Request request)
{
  DBConnection dbc(db_dir);
  string user = session.first.at("user");
  dbc.select("messages");
  Query params = postParams(request);
  list<string> ids = lookupParams("Conversa", params);
  for (auto it = ids.cbegin(); it != ids.cend(); it++) {
      dbc.delete_rows("messages", "id", *it);
  }
}

  

