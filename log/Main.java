package mp.br.quarkussocial;


import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    ServicoInjetado servico;

    @Override
    public int run(String... args) throws Exception {

        if(args.length > 0){
            servico.chamar(args[0]);
        }else{
            servico.chamar("");
        }

        return 0;
    }

/*
    public static void main(String... args) {
        Quarkus.run(HelloWorldMain.class, args);
    }
*/
}
