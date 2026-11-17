package com.fitapp.repository

import com.fitapp.data.local.dao.AlunoDao
import com.fitapp.data.local.dao.ProfessorDao
import com.fitapp.data.local.entity.Aluno
import com.fitapp.data.local.entity.Professor
import com.fitapp.utils.Resultado
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** Testes das regras de negocio de conta unica e vinculo, com DAOs falsos (em memoria). */
class UsuarioRepositoryTest {

    private class FakeProfessorDao : ProfessorDao {
        val dados = mutableListOf<Professor>()
        override suspend fun inserir(professor: Professor): Long {
            val id = (dados.size + 1).toLong(); dados.add(professor.copy(idProfessor = id)); return id
        }
        override suspend fun buscarPorEmail(email: String) = dados.firstOrNull { it.email == email }
        override suspend fun buscarPorCodigo(codigo: String) = dados.firstOrNull { it.codigoVinculacao == codigo }
        override suspend fun codigoExiste(codigo: String) = dados.any { it.codigoVinculacao == codigo }
    }
    private class FakeAlunoDao : AlunoDao {
        val dados = mutableListOf<Aluno>()
        override suspend fun inserir(aluno: Aluno): Long {
            val id = (dados.size + 1).toLong(); dados.add(aluno.copy(idAluno = id)); return id
        }
        override suspend fun buscarPorEmail(email: String) = dados.firstOrNull { it.email == email }
        override suspend fun buscarPorId(id: Long) = dados.firstOrNull { it.idAluno == id }
        override suspend fun listarPorProfessor(idProfessor: Long) = dados.filter { it.idProfessor == idProfessor }
    }
    private fun repo() = UsuarioRepository(FakeProfessorDao(), FakeAlunoDao())

    @Test fun `nao permite dois cadastros com o mesmo e-mail`() = runBlocking {
        val r = repo()
        assertTrue(r.cadastrarProfessor("Ana", "ana@fit.com", "senha123").ehSucesso)
        assertTrue(r.cadastrarProfessor("Outra", "ana@fit.com", "senha123") is Resultado.Erro)
    }
    @Test fun `rejeita e-mail invalido`() = runBlocking {
        assertTrue(repo().cadastrarProfessor("Ana", "email-errado", "senha123") is Resultado.Erro)
    }
    @Test fun `aluno so se cadastra com codigo valido`() = runBlocking {
        assertTrue(repo().cadastrarAluno("Bia", "bia@fit.com", "senha123", "XXXXXX") is Resultado.Erro)
    }
    @Test fun `aluno com codigo valido fica vinculado ao professor`() = runBlocking {
        val r = repo()
        val prof = r.cadastrarProfessor("Ana", "ana@fit.com", "senha123") as Resultado.Sucesso
        val aluno = r.cadastrarAluno("Bia", "bia@fit.com", "senha123", prof.dado.codigoVinculacao)
        assertTrue(aluno.ehSucesso)
        assertEquals(prof.dado.idProfessor, (aluno as Resultado.Sucesso).dado.idProfessor)
    }
}
