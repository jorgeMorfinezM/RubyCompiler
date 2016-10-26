puts "Hello World in Ruby";
      class pajaro    
         def asear
             print "Me estoy limpiando las plumas";
         end;
         def vuela
             print "estoy volando";
         end;
      end; 
       class PinguinoPajaro
          def vuela
               print "lo siento, yo solo nado";
          end;
       end;
       
       class Greeter
         def initialize(name)
           name = name.capitalize;
         end;
       
         def salute
           puts "Hello name " + @name;
         end;
       end;
	   
       g = Greeter.new;
       
       g.salute;
       
       puts "Como te llamas?";
       name = gets.chomp;
       puts "Hola " + name;
       
       print "El Tiempo en Ruby: ";
       print "Hoy es: ";
       hoy = Time.now;
       puts hoy;
       puts hoy.day
       puts hoy.month
       puts hoy.year
       puts hoy.hour
       puts hoy.min
       puts hoy.sec
       puts hoy.zone
       
       itver = "Instituto Tecnologico de Veracruz";
       puts itver;
       
       conta = 0;
       conta2 = 5;
       print('Estructura for en Ruby:');
       for i in ["a".."z"]
           puts i;
     	end;
       
   	print('Estructura While en Ruby:');
       while conta < conta2
           conta2 = conta + conta2;
           conta += conta;
           conta2 -= conta2;
       end;
       puts conta;
       puts conta2;
       
       def fact(n)
           if n == 0
              1;
                       n * fact(n-1);
           end;
       end;
       puts fact(ARGV[0].to_i);
       
       def identidad
           print "soy una persona mayor";
       end;
       
       puts "Ifs anidados en Ruby";
       	def tarifatren(edad=0)
       	    if(edad <= 12)
       	        print 'tarifa reducida';
                   if(edad == 0)
                       puts "no se cobra tarifa";
                   end;
                   else
           			     print "tarifa normal";
               end;
           end;
       
       print ('Estructura do en Ruby');
       conta3 = 0;
       conta4 = 0;
       loop do
           conta3 = conta3 + conta4;
           conta3 += conta3;
           conta4 -= conta4;
       end
       pusts conta3;
       puts conta4;
       
       print ('Estructura case en Ruby');
       year = 2000;
       leap = case
           when year % 400 == 0 then true;
           when year % 100 == 0 then false;
           else year % 4 == 0;
       end;
       puts leap;
       
       def constModule(pi = 3.1416, str="hola")
         	puts pi + " " + str;
           print c;
       end;
       
       def constants
       	print C1 + C2 + C3;
       end;
       
       class Progra
       	def materia(calificacion)
            	if(calificacion >= 70)
           	    print "soy una persona que pasa la materia";
           	else
           	    print "no paso la materia";
           	end;
           end;
       end;
       
       class Novia
        	def tipo(cuerpo)
               if(cuerpo >= 90)
                   print "anda con ella";
           	else
           	    print "buscate otra de tu tipo";
           	end;
           end;
       end;
       
       class Perro
           def initialize(raza, nombre)
       	    @nombre = nombre;
           	@raza = raza;
           end;
       
           de saludar
               puts "soy un perro de raza: " + raza;
               puts "mi nombre es: " + nombre;
               puts "Soy de raza: ' + @raza ' y mi nombre es: ' + @nombre";
           end;
   		
   		conta3 = 4;
   		conta4 = 5;
       end;
