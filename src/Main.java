import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Informe a key(Hexadecimal e separado por virgula!: ");
        String key = scanner.nextLine().trim().replace(" ","").replace(","," ");
        //String key = "41,45,49,4d,42,46,4a,4e,43,47,4b,4f,44,48,4c,50".trim().replace(" ","").replace(","," ");
        System.out.println("Informe o diretorio do arquivo que você quer criptografar:");
        String diretorio  = scanner.nextLine().trim();
        System.out.println("Nome do arquivo destino?:");
        String nome = scanner.nextLine().trim();


        byte[] data = Files.readAllBytes(Paths.get(diretorio));
        String[] vet = new String[data.length];
        for(int i=0;i<data.length;i++){
            vet[i] = Integer.toHexString(Integer.parseInt(String.valueOf(data[i])));
        }

        String[] keyVet = key.split(" ");
        List<Integer> chave = new ArrayList<>();
        int[] textoSimples = new int[16];
        for(int i=0;i<16;i++){
            textoSimples[i] = Integer.parseInt(vet[i],16);
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
        try ( OutputStreamWriter outputWriter = new OutputStreamWriter(new FileOutputStream(nome+".txt"), StandardCharsets.UTF_8)) {
            outputWriter.write(Integer.toHexString(cifra[0])+" "+Integer.toHexString(cifra[1])+" "+Integer.toHexString(cifra[2])+" "+Integer.toHexString(cifra[3])+"\r\n"+
                    Integer.toHexString(cifra[4])+" "+Integer.toHexString(cifra[5])+" "+Integer.toHexString(cifra[6])+" "+Integer.toHexString(cifra[7])+"\r\n"+
                            Integer.toHexString(cifra[8])+" "+Integer.toHexString(cifra[9])+" "+Integer.toHexString(cifra[10])+" "+Integer.toHexString(cifra[11])+"\r\n"+
                                    Integer.toHexString(cifra[12])+" "+Integer.toHexString(cifra[13])+" "+Integer.toHexString(cifra[14])+" "+Integer.toHexString(cifra[15])+"\r\n");
        }catch (Exception e){

        }

    }
}