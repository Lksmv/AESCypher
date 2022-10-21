import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String key = "0x41 0x45 0x49 0x4d 0x42 0x46 0x4a 0x4e 0x43 0x47 0x4b 0x4f 0x44 0x48 0x4c 0x50";
        key = key.replace("0x","");
        String[] keyVet = key.split(" ");
        List<Integer> chave = new ArrayList<>();
        int count = 0;
        Arrays.stream(keyVet).forEach(values -> {
            chave.add(Integer.parseInt(values,16));
        });
        System.out.println(chave);
        List<List<Integer>> roundKeys = new ArrayList<>();
        roundKeys.add(chave);
        AES aes = new AES();
        for (int i = 1;i<11;i++) {
            int[] etapa1 = aes.pegarUltimaPalavra(roundKeys.get(i - 1));
            int[] etapa2 = aes.rotacionarPalavra(etapa1);
            int[] etapa3 = aes.substituirBytes(etapa2);
            int[] etapa4 = aes.gerarRoundConstant(i);
            int[] etapa5 = aes.xorEntre(etapa3,etapa4);
            int[] etapa6 = aes.xorEntre(etapa5,aes.pegarPrimeiraPalavra(roundKeys.get(i-1)));
            System.out.println("");
        }
    }
}