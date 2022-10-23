import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String key = "0x41 0x45 0x49 0x4d 0x42 0x46 0x4a 0x4e 0x43 0x47 0x4b 0x4f 0x44 0x48 0x4c 0x50";
        String[] texto = "0x44 0x4e 0x56 0x4e 0x45 0x56 0x49 0x54 0x53 0x4f 0x4d 0x4f 0x45 0x4c 0x45 0x21".replace("0x","").split(" ");
        key = key.replace("0x","");
        String[] keyVet = key.split(" ");
        List<Integer> chave = new ArrayList<>();
        int[] textoSimples = new int[16];
        for(int i=0;i<16;i++){
            textoSimples[i] = Integer.parseInt(texto[i],16);
        }
        Arrays.stream(keyVet).forEach(values -> {
            chave.add(Integer.parseInt(values,16));
        });
        System.out.println(chave);
        List<List<Integer>> roundKeys = new ArrayList<>();
        roundKeys.add(chave);
        AES aes = new AES();
        for (int i = 1;i<11;i++) {
            int[] etapa1 = aes.pegarPalavra(roundKeys.get(i - 1),3);
            int[] etapa2 = aes.rotacionarPalavra(etapa1);
            int[] etapa3 = aes.substituirBytes(etapa2);
            int[] etapa4 = aes.gerarRoundConstant(i);
            int[] etapa5 = aes.xorEntre(etapa3,etapa4);
            int[] etapa6 = aes.xorEntre(etapa5,aes.pegarPalavra(roundKeys.get(i-1),0));
            int[] segunda = aes.xorEntre(etapa6,aes.pegarPalavra(roundKeys.get(i-1),1));
            int[] terceira = aes.xorEntre(segunda,aes.pegarPalavra(roundKeys.get(i-1),2));
            int[] quarta = aes.xorEntre(terceira,aes.pegarPalavra(roundKeys.get(i-1),3));
            roundKeys.add(aes.setarRoundKey(etapa6,segunda,terceira,quarta));
        }
        int[] cifra = aes.xorEntre(textoSimples,aes.toVector(roundKeys.get(0)));
        for(int i=0;i<9;i++){
            cifra = aes.substituirBytes(cifra);
            cifra = aes.shiftRows(cifra);
            cifra = aes.mixedColumns(cifra);
            cifra = aes.xorEntre(cifra,aes.toVector(roundKeys.get(i+1)));
        }
        cifra = aes.substituirBytes(cifra);
        cifra = aes.shiftRows(cifra);
        cifra = aes.xorEntre(cifra,aes.toVector(roundKeys.get(10)));
        System.out.println(Integer.toHexString(cifra[0])+" "+Integer.toHexString(cifra[1])+" "+Integer.toHexString(cifra[2])+" "+Integer.toHexString(cifra[3]));
        System.out.println(Integer.toHexString(cifra[4])+" "+Integer.toHexString(cifra[5])+" "+Integer.toHexString(cifra[6])+" "+Integer.toHexString(cifra[7]));
        System.out.println(Integer.toHexString(cifra[8])+" "+Integer.toHexString(cifra[9])+" "+Integer.toHexString(cifra[10])+" "+Integer.toHexString(cifra[11]));
        System.out.println(Integer.toHexString(cifra[12])+" "+Integer.toHexString(cifra[13])+" "+Integer.toHexString(cifra[14])+" "+Integer.toHexString(cifra[15]));


    }
}