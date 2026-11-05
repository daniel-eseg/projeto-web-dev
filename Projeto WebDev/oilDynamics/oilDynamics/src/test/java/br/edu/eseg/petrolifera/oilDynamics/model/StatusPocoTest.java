package br.edu.eseg.petrolifera.oilDynamics.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** RN-02: transições válidas do status do poço. Teste unitário puro, sem subir o Spring. */
class StatusPocoTest {

	@Test
	void ativoPodeIrParaManutencaoOuParado() {
		assertTrue(StatusPoco.ATIVO.podeMudarPara(StatusPoco.MANUTENCAO));
		assertTrue(StatusPoco.ATIVO.podeMudarPara(StatusPoco.PARADO));
	}

	@Test
	void manutencaoPodeIrParaAtivoOuParado() {
		assertTrue(StatusPoco.MANUTENCAO.podeMudarPara(StatusPoco.ATIVO));
		assertTrue(StatusPoco.MANUTENCAO.podeMudarPara(StatusPoco.PARADO));
	}

	@Test
	void paradoSoPodeIrParaManutencao() {
		assertTrue(StatusPoco.PARADO.podeMudarPara(StatusPoco.MANUTENCAO));
		assertFalse(StatusPoco.PARADO.podeMudarPara(StatusPoco.ATIVO));
	}

	@Test
	void manterOMesmoStatusEhPermitido() {
		for (StatusPoco s : StatusPoco.values()) {
			assertTrue(s.podeMudarPara(s));
		}
	}

}
