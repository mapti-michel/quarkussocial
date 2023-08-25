package mp.br.quarkussocial;


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;

@Dependent
public class ServicoInjetado {

    @PostConstruct
    void postConstruct(){
        System.out.println("Serviço Injetado");
    }

    void chamar(String var){
        System.out.println("Serviço com variável");
    }

}
