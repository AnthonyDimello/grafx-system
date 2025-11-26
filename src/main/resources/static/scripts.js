/* scripts.js - GRAFX utilities integrados ao Spring Boot */

const API_BASE = '/api';

/* ==========================================================
   Funções auxiliares
   ========================================================== */

// Formatação de telefone
function formatPhone(v) {
    v = String(v).replace(/\D/g, "").slice(0, 10);
    if (v.length <= 2) return v;
    if (v.length <= 6) return v.replace(/(\d{2})(\d+)/, "$1-$2");
    return v.replace(/(\d{2})(\d{4})(\d+)/, "$1-$2-$3");
}

// Formatação de CPF
function formatCPF(v) {
    v = String(v).replace(/\D/g, "").slice(0, 11);
    if (v.length <= 3) return v;
    if (v.length <= 6) return v.replace(/(\d{3})(\d+)/, "$1.$2");
    if (v.length <= 9) return v.replace(/(\d{3})(\d{3})(\d+)/, "$1.$2.$3");
    return v.replace(/(\d{3})(\d{3})(\d{3})(\d+)/, "$1.$2.$3-$4");
}

// Escape HTML para prevenir XSS
function escapeHtml(text) {
    if (!text) return "";
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Validação de email
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Mostrar loading
function showLoading(button) {
    const originalText = button.innerHTML;
    button.innerHTML = '<span class="loading"></span> Processando...';
    button.disabled = true;
    return originalText;
}

// Esconder loading
function hideLoading(button, originalText) {
    button.innerHTML = originalText;
    button.disabled = false;
}

/* ==========================================================
   Aplicação de máscaras
   ========================================================== */
document.addEventListener("DOMContentLoaded", function () {
    const phoneInputs = document.querySelectorAll('input[id*="telefone"], .mask-telefone');
    const cpfInputs = document.querySelectorAll('input[id*="cpf"], .mask-cpf');

    phoneInputs.forEach(input => {
        input.addEventListener('input', (e) => {
            e.target.value = formatPhone(e.target.value);
        });
        
        // Validação no blur
        input.addEventListener('blur', (e) => {
            if (e.target.value && !/^\d{2}-\d{4}-\d{4}$/.test(e.target.value)) {
                e.target.classList.add('is-invalid');
            } else {
                e.target.classList.remove('is-invalid');
            }
        });
    });

    cpfInputs.forEach(input => {
        input.addEventListener('input', (e) => {
            e.target.value = formatCPF(e.target.value);
        });
        
        // Validação no blur
        input.addEventListener('blur', (e) => {
            if (e.target.value && !/^\d{3}\.\d{3}\.\d{3}-\d{2}$/.test(e.target.value)) {
                e.target.classList.add('is-invalid');
            } else {
                e.target.classList.remove('is-invalid');
            }
        });
    });
});

/* ==========================================================
   Cadastro de Cliente
   ========================================================== */
if (document.getElementById('formCliente')) {
    document.getElementById('formCliente').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const submitButton = e.target.querySelector('button[type="submit"]');
        const originalText = showLoading(submitButton);

        const cliente = {
            nome: document.getElementById('c_nome').value.trim(),
            telefone: document.getElementById('c_telefone').value.trim(),
            cpf: document.getElementById('c_cpf').value.trim(),
            email: document.getElementById('c_email').value.trim(),
            endereco: document.getElementById('c_endereco').value.trim()
        };

        // Validações client-side
        if (!cliente.nome || !cliente.telefone || !cliente.cpf || !cliente.email || !cliente.endereco) {
            alert('⚠️ Por favor, preencha todos os campos obrigatórios.');
            hideLoading(submitButton, originalText);
            return;
        }

        if (!isValidEmail(cliente.email)) {
            alert('⚠️ Por favor, insira um email válido.');
            hideLoading(submitButton, originalText);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/clientes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(cliente)
            });

            const result = await response.json();

            if (result.status === 'success') {
                alert('✅ ' + result.message);
                e.target.reset();
            } else {
                alert('❌ ' + result.message);
            }
        } catch (error) {
            console.error('Erro:', error);
            alert('❌ Erro ao conectar com o servidor. Tente novamente.');
        } finally {
            hideLoading(submitButton, originalText);
        }
    });
}

/* ==========================================================
   Cadastro de Produto
   ========================================================== */
if (document.getElementById('formProduto')) {
    document.getElementById('formProduto').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const submitButton = e.target.querySelector('button[type="submit"]');
        const originalText = showLoading(submitButton);

        const produto = {
            descricao: document.getElementById('p_desc').value.trim(),
            estoque: parseInt(document.getElementById('p_qtde').value),
            valor: parseFloat(document.getElementById('p_valor').value)
        };

        // Validações client-side
        if (!produto.descricao || isNaN(produto.estoque) || isNaN(produto.valor)) {
            alert('⚠️ Por favor, preencha todos os campos obrigatórios corretamente.');
            hideLoading(submitButton, originalText);
            return;
        }

        if (produto.estoque < 0) {
            alert('⚠️ O estoque não pode ser negativo.');
            hideLoading(submitButton, originalText);
            return;
        }

        if (produto.valor <= 0) {
            alert('⚠️ O valor deve ser maior que zero.');
            hideLoading(submitButton, originalText);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/produtos`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(produto)
            });

            const result = await response.json();

            if (result.status === 'success') {
                alert('✅ ' + result.message);
                e.target.reset();
            } else {
                alert('❌ ' + result.message);
            }
        } catch (error) {
            console.error('Erro:', error);
            alert('❌ Erro ao conectar com o servidor. Tente novamente.');
        } finally {
            hideLoading(submitButton, originalText);
        }
    });
}

/* ==========================================================
   Cadastro de Atendente
   ========================================================== */
if (document.getElementById('formAtendente')) {
    document.getElementById('formAtendente').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const submitButton = e.target.querySelector('button[type="submit"]');
        const originalText = showLoading(submitButton);

        const atendente = {
            nome: document.getElementById('a_nome').value.trim(),
            telefone: document.getElementById('a_telefone').value.trim(),
            cpf: document.getElementById('a_cpf').value.trim(),
            senha: document.getElementById('a_senha').value.trim()
        };

        // Validações client-side
        if (!atendente.nome || !atendente.telefone || !atendente.cpf || !atendente.senha) {
            alert('⚠️ Por favor, preencha todos os campos obrigatórios.');
            hideLoading(submitButton, originalText);
            return;
        }

        if (atendente.senha.length < 6) {
            alert('⚠️ A senha deve ter pelo menos 6 caracteres.');
            hideLoading(submitButton, originalText);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/atendentes`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(atendente)
            });

            const result = await response.json();

            if (result.status === 'success') {
                alert('✅ ' + result.message);
                e.target.reset();
            } else {
                alert('❌ ' + result.message);
            }
        } catch (error) {
            console.error('Erro:', error);
            alert('❌ Erro ao conectar com o servidor. Tente novamente.');
        } finally {
            hideLoading(submitButton, originalText);
        }
    });
}

/* ==========================================================
   Sistema de Busca
   ========================================================== */
if (document.getElementById('selectBusca')) {
    document.getElementById('selectBusca').addEventListener('change', async (e) => {
        const tipo = e.target.value;
        const thead = document.getElementById('theadBusca');
        const tbody = document.getElementById('tbodyBusca');

        thead.innerHTML = '';
        tbody.innerHTML = '<tr><td><span class="loading"></span> Carregando...</td></tr>';

        if (!tipo) {
            tbody.innerHTML = '<tr><td>Selecione uma opção para carregar os dados.</td></tr>';
            return;
        }

        try {
            let endpoint = '';
            let columns = [];

            switch(tipo) {
                case 'clientes':
                    endpoint = `${API_BASE}/clientes`;
                    columns = ['Nome', 'Telefone', 'CPF', 'E-mail', 'Endereço'];
                    break;
                case 'produtos':
                    endpoint = `${API_BASE}/produtos`;
                    columns = ['Descrição', 'Estoque', 'Valor (R$)'];
                    break;
                case 'atendentes':
                    endpoint = `${API_BASE}/atendentes`;
                    columns = ['Nome', 'Telefone', 'CPF'];
                    break;
                case 'vendas':
                    endpoint = `${API_BASE}/vendas`;
                    columns = ['Cliente', 'Produto', 'Quantidade', 'Total (R$)', 'Data', 'Atendente'];
                    break;
            }

            const response = await fetch(endpoint);
            if (!response.ok) throw new Error('Erro ao buscar dados');
            
            const dados = await response.json();

            // Cria cabeçalho
            thead.innerHTML = `<tr>${columns.map(col => `<th>${col}</th>`).join('')}</tr>`;

            // Cria corpo da tabela
            if (dados.length === 0) {
                tbody.innerHTML = `<tr><td colspan="${columns.length}">Nenhum registro encontrado.</td></tr>`;
            } else {
                tbody.innerHTML = dados.map(item => {
                    switch(tipo) {
                        case 'clientes':
                            return `<tr>
                                <td>${escapeHtml(item.nome)}</td>
                                <td>${escapeHtml(item.telefone)}</td>
                                <td>${escapeHtml(item.cpf)}</td>
                                <td>${escapeHtml(item.email)}</td>
                                <td>${escapeHtml(item.endereco)}</td>
                            </tr>`;
                        case 'produtos':
                            return `<tr>
                                <td>${escapeHtml(item.descricao)}</td>
                                <td>${escapeHtml(item.estoque)}</td>
                                <td>R$ ${escapeHtml(item.valor.toFixed(2))}</td>
                            </tr>`;
                        case 'atendentes':
                            return `<tr>
                                <td>${escapeHtml(item.nome)}</td>
                                <td>${escapeHtml(item.telefone)}</td>
                                <td>${escapeHtml(item.cpf)}</td>
                            </tr>`;
                        case 'vendas':
                            return `<tr>
                                <td>${escapeHtml(item.nomeCliente)}</td>
                                <td>${escapeHtml(item.produto.descricao)}</td>
                                <td>${escapeHtml(item.quantidade)}</td>
                                <td>R$ ${escapeHtml(item.valorTotal.toFixed(2))}</td>
                                <td>${escapeHtml(new Date(item.dataVenda).toLocaleDateString('pt-BR'))}</td>
                                <td>${escapeHtml(item.atendente.nome)}</td>
                            </tr>`;
                        default:
                            return '<tr><td>Formato não suportado</td></tr>';
                    }
                }).join('');
            }
        } catch (error) {
            console.error('Erro:', error);
            tbody.innerHTML = '<tr><td>❌ Erro ao carregar dados do servidor.</td></tr>';
        }
    });
}

/* ==========================================================
   Sistema de Vendas
   ========================================================== */
if (document.getElementById('formVenda')) {
    document.addEventListener('DOMContentLoaded', async () => {
        await carregarDadosVenda();
        
        // Define data atual como padrão
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('v_data').value = today;
    });

    async function carregarDadosVenda() {
        try {
            const [produtosResponse, atendentesResponse] = await Promise.all([
                fetch(`${API_BASE}/produtos`),
                fetch(`${API_BASE}/atendentes`)
            ]);

            if (!produtosResponse.ok || !atendentesResponse.ok) {
                throw new Error('Erro ao carregar dados');
            }

            const produtos = await produtosResponse.json();
            const atendentes = await atendentesResponse.json();

            const selectProduto = document.getElementById('v_produto');
            const selectAtendente = document.getElementById('v_atendente');

            // Preenche produtos
            selectProduto.innerHTML = '<option value="">Selecione...</option>';
            produtos.forEach(p => {
                const option = document.createElement('option');
                option.value = p.id;
                option.textContent = p.descricao;
                option.setAttribute('data-valor', p.valor);
                option.setAttribute('data-estoque', p.estoque);
                selectProduto.appendChild(option);
            });

            // Preenche atendentes
            selectAtendente.innerHTML = '<option value="">Selecione...</option>';
            atendentes.forEach(a => {
                const option = document.createElement('option');
                option.value = a.id;
                option.textContent = a.nome;
                selectAtendente.appendChild(option);
            });

            // Event listeners para cálculos
            selectProduto.addEventListener('change', (e) => {
                const selectedOption = e.target.options[e.target.selectedIndex];
                const valor = selectedOption.getAttribute('data-valor');
                const estoque = selectedOption.getAttribute('data-estoque');
                
                document.getElementById('v_valor').value = valor || '';
                
                // Mostra estoque disponível
                if (estoque) {
                    const qtdeInput = document.getElementById('v_qtde');
                    qtdeInput.setAttribute('max', estoque);
                    qtdeInput.setAttribute('title', `Estoque disponível: ${estoque} unidades`);
                }
                
                calcularTotal();
            });

            document.getElementById('v_qtde').addEventListener('input', calcularTotal);

            function calcularTotal() {
                const valor = parseFloat(document.getElementById('v_valor').value) || 0;
                const qtde = parseInt(document.getElementById('v_qtde').value) || 0;
                document.getElementById('v_total').value = (valor * qtde).toFixed(2);
            }
        } catch (error) {
            console.error('Erro ao carregar dados da venda:', error);
            alert('❌ Erro ao carregar dados do servidor.');
        }
    }

    document.getElementById('formVenda').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const submitButton = e.target.querySelector('button[type="submit"]');
        const originalText = showLoading(submitButton);

        const venda = {
            nomeCliente: document.getElementById('v_cliente').value.trim(),
            cpf: document.getElementById('v_cpf').value.trim(),
            produto: {
                id: parseInt(document.getElementById('v_produto').value)
            },
            valorUnitario: parseFloat(document.getElementById('v_valor').value),
            quantidade: parseInt(document.getElementById('v_qtde').value),
            valorTotal: parseFloat(document.getElementById('v_total').value),
            formaPagamento: document.getElementById('v_pagamento').value,
            atendente: {
                id: parseInt(document.getElementById('v_atendente').value)
            },
            dataVenda: document.getElementById('v_data').value
        };

        // Validações client-side
        if (!venda.nomeCliente || !venda.cpf || !venda.produto.id || !venda.atendente.id || 
            !venda.formaPagamento || !venda.dataVenda) {
            alert('⚠️ Por favor, preencha todos os campos obrigatórios.');
            hideLoading(submitButton, originalText);
            return;
        }

        if (venda.quantidade < 1) {
            alert('⚠️ A quantidade deve ser pelo menos 1.');
            hideLoading(submitButton, originalText);
            return;
        }

        try {
            const response = await fetch(`${API_BASE}/vendas`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(venda)
            });

            const result = await response.json();

            if (result.status === 'success') {
                alert('✅ ' + result.message);
                e.target.reset();
                
                // Recarrega dados para atualizar estoque
                await carregarDadosVenda();
                
                // Define data atual novamente
                const today = new Date().toISOString().split('T')[0];
                document.getElementById('v_data').value = today;
            } else {
                alert('❌ ' + result.message);
            }
        } catch (error) {
            console.error('Erro:', error);
            alert('❌ Erro ao conectar com o servidor. Tente novamente.');
        } finally {
            hideLoading(submitButton, originalText);
        }
    });
}

/* ==========================================================
   Utilitários Gerais
   ========================================================== */

// Tratamento de erro global
window.addEventListener('error', function(e) {
    console.error('Erro global:', e.error);
});

