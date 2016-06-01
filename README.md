# Reordenador de instruções

Este foi um projeto da disciplina de Introdução a organização e arquitetura de computadores (IOAC) para reordenar instruções assembly de um grafo feito em java.

O programa deve receber um arquivo .txt com um grafo de dependencias de instruções assembly e retornar um novo arquivo .txt com o grafo reordenado para reduzir a quantidade de ciclos necesários.

O reordenamento considera um pipeline de 5 estágios e que a escrita (WB) e leitura (ID) podem ser executadas no mesmo ciclo.
Também foi implementado uma função para renomear registradores afim de evitar possíveis falhas devido a falsas dependencias onde duas instruções escreviam no mesmo registrador.

Foi feito como extra uma interface gráfica com JavaFX, com ela é possivel buscar um arquivo com o explorer, escolher um lugar para salvar o arquivo e visualizar o grafo como uma tabela.

branch patch -> Ultima versão atualizada, com correções e updates

branch console version -> Versão console funcional e revisada

branch interface version -> Versão com interface funcional e revisada

branch master -> Versão final do projeto, com interface gráfica

Desenvolvido por Will Lucena
