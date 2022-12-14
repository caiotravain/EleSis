/**
 * Curso: Elementos de Sistemas
 * Arquivo: Parser.java
 */

package assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Encapsula o código de leitura. Carrega as instruções na linguagem assembly,
 * analisa, e oferece acesso as partes da instrução  (campos e símbolos).
 * Além disso, remove todos os espaços em branco e comentários.
 */
public class Parser {
    private Boolean jump = false;
    private final BufferedReader fileReader;
    public String inputFile;		        // arquivo de leitura
    public int lineNumber = 0;		     	// linha atual do arquivo (nao do codigo gerado)
    public String currentCommand = "";      // comando atual
    public String currentLine;			    // linha de codigo atual
    public Boolean isJmp = false;


    /** Enumerator para os tipos de comandos do Assembler. */
    public enum CommandType {
        A_COMMAND,      // comandos LEA, que armazenam no registrador A
        C_COMMAND,      // comandos de calculos
        L_COMMAND       // comandos de Label (símbolos)
    }

    /**
     * Abre o arquivo de entrada NASM e se prepara para analisá-lo.
     * @param file arquivo NASM que será feito o parser.
     */
    public Parser(String file) throws FileNotFoundException {
        this.inputFile = file;
        this.fileReader = new BufferedReader(new FileReader(file));
        this.lineNumber = 0;
    }

    // fecha o arquivo de leitura
    public void close() throws IOException {
        fileReader.close();
    }

    /**
     * Carrega uma instrução e avança seu apontador interno para o próxima
     * linha do arquivo de entrada. Caso não haja mais linhas no arquivo de
     * entrada o método retorna "Falso", senão retorna "Verdadeiro".
     * @return Verdadeiro se ainda há instruções, Falso se as instruções terminaram.
     */
    public Boolean advance() {

        while(true){

            try {
                currentLine = fileReader.readLine();
                try{
                    String teste = currentLine.replaceAll(" ","");
                    if (jump == true){
                        if (!teste.equals ("nop")){
                            currentLine = "nop";
                            lineNumber--;
                            jump = false;


                        }
                        else {
                            jump = false;
                        }
                    }

                    if (teste.charAt(0) == 'j'){
                        jump = true;
                    }
                }
                catch (Exception e){

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            lineNumber++;
            if (currentLine == null)
                return false;  // caso não haja mais comandos
            currentCommand = currentLine.replaceAll(";.*$", "").trim();
            System.out.println(currentCommand);
            if (currentCommand.equals(""))
                continue;
            return true;   // caso um comando seja encontrado
        }
    }

    /**
     * Retorna o comando "intrução" atual (sem o avanço)
     * @return a instrução atual para ser analilisada
     */
    public String command() {
        /* ja esta pronto */
        return currentCommand;
    }

    /**
     * Retorna o tipo da instrução passada no argumento:
     *  A_COMMAND para leaw, por exemplo leaw $1,%A
     *  L_COMMAND para labels, por exemplo Xyz: , onde Xyz é um símbolo.
     *  C_COMMAND para todos os outros comandos
     * @param  command instrução a ser analisada.
     * @return o tipo da instrução.
     */
    public CommandType commandType(String command) {
        String splitado = command.split(" ")[0];
        if (command.contains(":")){
            return CommandType.L_COMMAND;
        }
        else if (splitado.equals("leaw")){
            return CommandType.A_COMMAND;
        }
        else{
            return CommandType.C_COMMAND;
        }
    }

    /**
     * Retorna o símbolo ou valor numérico da instrução passada no argumento.
     * Deve ser chamado somente quando commandType() é A_COMMAND.
     * @param  command instrução a ser analisada.
     * @return somente o símbolo ou o valor número da instrução.
     */
    public String symbol(String command) {
        String splitado2 = "";
        /* TODO: implementar */
        String splitado = command.split(",")[0];
        try{
            splitado2 = splitado.split(" ")[1];}
        catch (Exception e){
            splitado2 = splitado;
        }
        String splitfinal = splitado2.replace("$", "");
        if (commandType(command) == CommandType.A_COMMAND){
            return splitfinal;
        }
        return null;
    }

    /**
     * Retorna o símbolo da instrução passada no argumento.
     * Deve ser chamado somente quando commandType() é L_COMMAND.
     * @param  command instrução a ser analisada.
     * @return o símbolo da instrução (sem os dois pontos).
     */
    public String label(String command) {
        /* TODO: implementar */
        String splitado = command.split(":")[0];
        String splitado2 = splitado.split(" ")[0];
        if (commandType(command) == CommandType.L_COMMAND){
            return splitado2;
        }
        return null;
    }

    /**
     * Separa os mnemônicos da instrução fornecida em tokens em um vetor de Strings.
     * Deve ser chamado somente quando CommandType () é C_COMMAND.
     * @param  command instrução a ser analisada.
     * @return um vetor de string contento os tokens da instrução (as partes do comando).
     */
    public String[] instruction(String command) {
        /* TODO: implementar */
        String replacement = command.replace(" ",",");
        String[] splitado = replacement.split(",");
        List<String> lista = Arrays.asList(splitado);
        List<String> lista2 = new ArrayList<String>();
        for (String a : lista){
            if (a != ""){
                lista2.add(a);

            }
        }

        if (lista2.size() == 1){
            String[] resultado = new String[]{lista2.get(0)};
            return resultado;
        }
        else if (lista2.size() == 2){
            String[] resultado = new String[]{lista2.get(0), lista2.get(1)};
            return resultado;
        }
        else if (lista2.size() == 3){
            String[] resultado = new String[]{lista2.get(0), lista2.get(1), lista2.get(2)};
            return resultado;
        }
        else if (lista2.size() == 4){
            String[] resultado = new String[]{lista2.get(0), lista2.get(1), lista2.get(2), lista2.get(3)};
            return resultado;
        }

        return null;
    }
    public String nop(String command){
        isJmp = true;
        if (isJmp){
            if (command.equals("nop")){
                return ("Esqueceu do NOP! Erro na linha" + currentLine);
            }
        }
        return null;
    }


}