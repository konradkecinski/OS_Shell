/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfejs_so;

import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Konrad
 */
public class Shell {
    Boolean system;
    Boolean session;
    String command;
    ArrayList<String> parts;
    String user;
    Shell()
    {
        system = false;
        session = false;
        command = null;
        parts = new ArrayList<>();
        user = null;
    }
    void system()
    {
        system=true;
        start();
        FileManagement filemanagement = new FileManagement();
        Process_container procmen = new Process_container();
        Scheduler scheduler = new Scheduler(procmen.get_by_PID(0));
        Memory memory = new Memory();
        while(system)
        {
        
        login();
        while(session)
        {
            Scanner sc = new Scanner(System.in);
            System.out.print("bigOS:\\User>");
            command=sc.nextLine();
            for(int i=0;i<command.length();i++)
            {
                if((int)command.charAt(i)<=90&&(int)command.charAt(i)>=65)
                {
                    int help = (int)command.charAt(i);
                    help=help+32;
                    String newCommand = command.substring(0,i)+(char)help+command.substring(i+1);
                    command=newCommand;
                }
            }
            cut();
            execute(procmen, filemanagement);
        }
        }
    }
   void cut()
   {
       parts.clear();
       String help="";
       for(int i=0;i<command.length();i++)
       {
           if(command.charAt(i)!=' ')
           {
               help=help+command.charAt(i);
           }
           else
           {
               parts.add(help);
               help="";
           }
           if(i==command.length()-1)
           {
               parts.add(help);
           }
           
       }
       
   }
    void start()
    {
        System.out.println("Tu jest logo bigOS");
    }
    void login()
    {
        while(true)
        {
        Scanner sc = new Scanner(System.in);
        System.out.printf("User: ");
        user = sc.nextLine();
        System.out.printf("Password: ");
        String password = sc.nextLine();
        // Zabezpieczeniowiec sprawdza poprawnosc i laczy
        // Jezeli polaczyl jest return i session=true
        // Jezeli nie, pytanie czy chce zakonczyc prace systemu
        session=true;
        return;
        }
    }
    void execute(Process_container procmen, FileManagement filemanagement)
    {
        switch(parts.get(0))
        {
            
                case "help":
                {
                 if(parts.size()==1)
                 System.out.println("help"); 
                 else
                        System.out.println("dir, shutdown, cp, cf, wf, df, tasklist, type");
                    break;
                }
                case "dir":
                {
                    if(parts.size()==1)
                    // Zabezpieczeniowiec sprawdza
                    filemanagement.readall();
                    else
                        System.out.println("Bledne argumenty");
                    break;
                }
                case "shutdown":
                {
                    if(parts.size()==1)
                    {
                    session=false;
                    system = false;
                    }
                    else if(parts.size()==2 && parts.get(1).equals("-l"))
                        session=false;
                    else
                        System.out.println("Bledne argumenty");
                    break;
                }
                case "df":
                {
                    if(parts.size()==2)
                    {
                        // Zabezpieczeniowiec -> sprawdza
                        int blad = filemanagement.delete(parts.get(1));
                        if(blad==0)
                        System.out.println("Usunieto "+parts.get(1));
                        else
                        {
                           System.out.println("Blad "+blad); 
                        }
                        
                    }
                    else
                        System.out.println("Bledne argumenty");
                    
                    break;
                }
                case "cf":
                {
                 if(parts.size()==2)
                    {
                        
                        int blad = filemanagement.create(parts.get(1),user);
                        if(blad==0)
                        {
                        System.out.println("Stworzono "+parts.get(1));
                        // Zabezpieczeniowiec wpisuje
                        }
                        else
                        {
                           System.out.println("Blad "+blad); 
                        }
                        
                    }
                    else
                        System.out.println("Bledne argumenty");
                    
                 break;
                }
                case "wf":
                {
                 if(parts.size()>2)
                    {
                        String buffor="";
                        for(int i=2;i<parts.size();i++)
                            buffor=buffor+parts.get(i);
                        // Zabezpieczeniowiec sprawdza czy mozna wpisac
                        int blad = filemanagement.write(parts.get(1),buffor);
                        if(blad==0)
                        {
                        System.out.println("Dopisano "+buffor);
                        
                        }
                        else
                        {
                           System.out.println("Blad "+blad); 
                        }
                        
                    }
                    else
                        System.out.println("Bledne argumenty");
                 break;
                }
                case "tasklist":
                {
                 if(parts.size()==1)
                 {
                 System.out.println("Wyswietla procesy");
                 procmen.show_all_processes();
                 }
                 else
                        System.out.println("Bledne argumenty");
                 break;
                }
                case "cp":
                {
                    if(parts.size()==4)
                    {
                        for(int i=0;i<parts.get(3).length();i++)
                            if(Character.isDigit(parts.get(3).charAt(i)));
                            else
                            {
                                System.out.println("Bledne argumenty");
                            }
                        procmen.create_process(parts.get(1),parts.get(2),Integer.parseInt(parts.get(3)));
                        System.out.println("Tworze proces");
                    }
                    else if(parts.size()==5)
                    {
                        for(int i=0;i<parts.get(3).length();i++)
                            if(Character.isDigit(parts.get(3).charAt(i)));
                            else
                            {
                                System.out.println("Bledne argumenty");
                            }
                        for(int i=0;i<parts.get(4).length();i++)
                            if(Character.isDigit(parts.get(4).charAt(i)));
                            else
                            {
                                System.out.println("Bledne argumenty");
                            }
                        procmen.create_process(parts.get(1),parts.get(2),Integer.parseInt(parts.get(3)),Integer.parseInt(parts.get(4)));
                        System.out.println("Tworze proces");
                    }
                    else
                        System.out.println("Bledne argumenty");
                    break;
                }
                case "type":
                {
                    if(parts.size()==2)
                    {
                        //Wypisanie zawartosci pliku parts[2]
                    }
                    else if(parts.size()==4)
                    {
                        if((int)parts.get(2).charAt(0)==62)
                            if(parts.get(2).length()== 2)
                            {
                                if((int)parts.get(2).charAt(1)==62)
                                {
                                // utworzenie nowego pliku parts[4] z zawartoscia pliku parts[2]
                                }
                                else
                                    System.out.println("Bledne argumenty");
                            }
                            else if (parts.get(2).length()==1)
                            {
                                // wpisanie zawartosci pliku parts[2] do istniejacego pliku parts[4]
                            }
                            else
                                System.out.println("Bledne argumenty");
                        else {
                            System.out.println("Bledne argumenty");
                        }
                    }
                    else
                    {
                        System.out.println("Bledne argumenty");
                    }
                    break;
                }
                case "net":
                {
                    if(parts.size()>1)
                    {
                    
                    if("user".equals(parts.get(1)))
                    {
                        if(parts.size()==3)
                        {
                            //Wypisanie info o uzytkowniku parts[2]
                        }
                        else if(parts.size()==4)
                        {
                            if("/delete".equals(parts.get(3)))
                            {
                                //usuwa uzytkownika parts[2]
                            }
                            else
                            {
                            System.out.println("Bledne argumenty");
                            }
                        }
                        else if(parts.size()==5)
                        {
                            if("/add".equals(parts.get(4)))
                            {
                                //Dodaje uzytkownika o nazwie parts[2] i hasle parts[3]
                            }
                            else
                            {
                            System.out.println("Bledne argumenty");
                            }
                        }
                        else
                        {
                            System.out.println("Bledne argumenty");
                        }
                        
                    }
                    else
                    {
                        System.out.println("Bledne argumenty");
                    }
                    }
                    else
                    {
                        
                    }
                    break;
                }
                default:
                {
                    
                    System.out.println("'"+command+ "' jest nieznana komenda.");
                    break;
                }
        }
        
    }
    
    
    
}
