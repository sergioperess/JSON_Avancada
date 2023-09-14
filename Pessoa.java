public class Pessoa {
    private String nome;
    private String sobrenome;
    private String signo;
    private String cpf;

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getSigno() {
        return signo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String toString() {
        return "Pessoa{" + "nome: " + nome + " sobrenome: " + sobrenome + " signo: " + signo + " cpf: " + cpf + "}";
    }

}
