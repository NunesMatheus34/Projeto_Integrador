package com.fitapp.repository

import com.fitapp.data.local.dao.AlunoDao
import com.fitapp.data.local.dao.ProfessorDao
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor
import com.fitapp.utils.GeradorCodigo
import com.fitapp.utils.Resultado
import com.fitapp.utils.SegurancaSenha
import com.fitapp.utils.Validador

/**
 * Regras de negocio de contas, autenticacao e vinculo.
 * Conta unica, vinculo aluno-professor por codigo, senha somente em hash.
 * Todo acesso ao banco e protegido por try/catch e convertido em Resultado.
 */
class UsuarioRepository(
    private val professorDao: ProfessorDao,
    private val alunoDao: AlunoDao
) {

    suspend fun cadastrarProfessor(nome: String, email: String, senha: String): Resultado<Professor> {
        validarDados(nome, email, senha)?.let { return it }
        return try {
            if (emailJaUsado(email)) return Resultado.Erro("Ja existe uma conta com este e-mail.")
            val professor = Professor(
                nome = nome.trim(), email = email.trim().lowercase(),
                senhaHash = SegurancaSenha.hash(senha), codigoVinculacao = gerarCodigoUnico()
            )
            val id = professorDao.inserir(professor)
            Resultado.Sucesso(professor.copy(idProfessor = id))
        } catch (e: Exception) {
            Resultado.Erro("Nao foi possivel concluir o cadastro. Tente novamente.")
        }
    }

    suspend fun cadastrarAluno(nome: String, email: String, senha: String, codigoProfessor: String): Resultado<Aluno> {
        validarDados(nome, email, senha)?.let { return it }
        if (codigoProfessor.isBlank()) return Resultado.Erro("Informe o codigo do seu professor.")
        return try {
            if (emailJaUsado(email)) return Resultado.Erro("Ja existe uma conta com este e-mail.")
            val professor = professorDao.buscarPorCodigo(codigoProfessor.trim().uppercase())
                ?: return Resultado.Erro("Codigo de professor invalido. Confira com seu treinador.")
            val aluno = Aluno(
                nome = nome.trim(), email = email.trim().lowercase(),
                senhaHash = SegurancaSenha.hash(senha), idProfessor = professor.idProfessor
            )
            val id = alunoDao.inserir(aluno)
            Resultado.Sucesso(aluno.copy(idAluno = id))
        } catch (e: Exception) {
            Resultado.Erro("Nao foi possivel concluir o cadastro. Tente novamente.")
        }
    }

    suspend fun autenticarAluno(email: String, senha: String): Resultado<Aluno> = try {
        val aluno = alunoDao.buscarPorEmail(email.trim().lowercase())
        if (aluno != null && SegurancaSenha.confere(senha, aluno.senhaHash)) Resultado.Sucesso(aluno)
        else Resultado.Erro("E-mail ou senha incorretos.")
    } catch (e: Exception) { Resultado.Erro("Falha ao entrar. Tente novamente.") }

    suspend fun autenticarProfessor(email: String, senha: String): Resultado<Professor> = try {
        val prof = professorDao.buscarPorEmail(email.trim().lowercase())
        if (prof != null && SegurancaSenha.confere(senha, prof.senhaHash)) Resultado.Sucesso(prof)
        else Resultado.Erro("E-mail ou senha incorretos.")
    } catch (e: Exception) { Resultado.Erro("Falha ao entrar. Tente novamente.") }

    suspend fun listarAlunos(idProfessor: Long): Resultado<List<Aluno>> = try {
        Resultado.Sucesso(alunoDao.listarPorProfessor(idProfessor))
    } catch (e: Exception) { Resultado.Erro("Nao foi possivel carregar os alunos.") }

    private fun validarDados(nome: String, email: String, senha: String): Resultado.Erro? = when {
        !Validador.nomeValido(nome) -> Resultado.Erro("Informe um nome valido.")
        !Validador.emailValido(email) -> Resultado.Erro("E-mail invalido.")
        !Validador.senhaValida(senha) -> Resultado.Erro("A senha deve ter ao menos 6 caracteres.")
        else -> null
    }

    private suspend fun emailJaUsado(email: String): Boolean {
        val e = email.trim().lowercase()
        return professorDao.buscarPorEmail(e) != null || alunoDao.buscarPorEmail(e) != null
    }

    private suspend fun gerarCodigoUnico(): String {
        var codigo = GeradorCodigo.gerar()
        while (professorDao.codigoExiste(codigo)) codigo = GeradorCodigo.gerar()
        return codigo
    }
}
