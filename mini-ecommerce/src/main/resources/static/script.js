// URL base da API - altere se necessário
const API_BASE_URL = 'http://localhost:8080/api/produtos';

let produtos = [];
let produtosFiltrados = [];
let currentView = 'dashboard';
let editingId = null;

// Elementos da navegação
const navDashboard = document.getElementById('nav-dashboard');
const navCadastrar = document.getElementById('nav-cadastrar');
const navListar = document.getElementById('nav-listar');

// Views
const dashboardView = document.getElementById('dashboard-view');
const formView = document.getElementById('form-view');
const listView = document.getElementById('list-view');

// Botões
const btnSalvar = document.getElementById('btn-salvar');
const btnBuscar = document.getElementById('btn-buscar');

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
  // Configurar data atual como padrão
  document.getElementById('data').valueAsDate = new Date();
  
  // Carregar dados iniciais
  carregarDados();
  
  // Configurar navegação
  navDashboard.addEventListener('click', (e) => {
    e.preventDefault();
    showView('dashboard');
    updateNavState('dashboard');
  });
  
  navCadastrar.addEventListener('click', (e) => {
    e.preventDefault();
    editingId = null;
    limparFormulario();
    showView('form');
    updateNavState('cadastrar');
  });
  
  navListar.addEventListener('click', (e) => {
    e.preventDefault();
    showView('list');
    updateNavState('listar');
  });
});

// Funções para consumir a API
async function carregarDados() {
  try {
    await Promise.all([
      carregarProdutos(),
      atualizarDashboard()
    ]);
  } catch (error) {
    console.error('Erro ao carregar dados:', error);
    showNotification('Erro ao carregar dados do servidor', 'error');
  }
}

async function carregarProdutos() {
  try {
    toggleLoading(true, 'Carregando produtos...');
    const response = await fetch(API_BASE_URL);
    if (!response.ok) throw new Error('Erro ao carregar produtos');
    produtos = await response.json();
    produtosFiltrados = [...produtos];
    aplicarFiltros();
    toggleLoading(false);
  } catch (error) {
    console.error('Erro:', error);
    showNotification('Erro ao carregar produtos', 'error');
    toggleLoading(false, 'Erro ao carregar produtos. Clique para tentar novamente.', true);
  }
}

// Funções de Filtro
function aplicarFiltros() {
  const termo = document.getElementById("search-input").value.toLowerCase();
  const categoria = document.getElementById("filter-categoria").value;
  const estoque = document.getElementById("filter-estoque").value;
  const preco = document.getElementById("filter-preco").value;
  const ordem = document.getElementById("filter-ordem").value;
  
  // Aplicar filtros
  produtosFiltrados = produtos.filter(produto => {
    // Filtro por termo de busca
    if (termo && !produto.nome.toLowerCase().includes(termo) && 
        !(produto.descricao && produto.descricao.toLowerCase().includes(termo))) {
      return false;
    }
    
    // Filtro por categoria
    if (categoria && produto.categoria !== categoria) {
      return false;
    }
    
    // Filtro por estoque
    if (estoque === "com-estoque" && produto.estoque <= 0) {
      return false;
    }
    if (estoque === "sem-estoque" && produto.estoque > 0) {
      return false;
    }
    if (estoque === "estoque-baixo" && produto.estoque >= 10) {
      return false;
    }
    
    // Filtro por preço
    if (preco) {
      const [min, max] = preco.split('-');
      if (max === '+') {
        if (produto.preco < parseFloat(min)) return false;
      } else {
        if (produto.preco < parseFloat(min) || produto.preco > parseFloat(max)) return false;
      }
    }
    
    return true;
  });
  
  // Ordenar produtos
  produtosFiltrados.sort((a, b) => {
    switch (ordem) {
      case 'nome':
        return a.nome.localeCompare(b.nome);
      case 'nome-desc':
        return b.nome.localeCompare(a.nome);
      case 'preco':
        return a.preco - b.preco;
      case 'preco-desc':
        return b.preco - a.preco;
      case 'data-recente':
        return new Date(b.dataEntrada) - new Date(a.dataEntrada);
      case 'data-antiga':
        return new Date(a.dataEntrada) - new Date(b.dataEntrada);
      default:
        return 0;
    }
  });
  
  // Atualizar contador
  document.getElementById('contador-produtos').textContent = 
    `Mostrando ${produtosFiltrados.length} de ${produtos.length} produtos`;
  
  // Renderizar produtos filtrados
  renderizarProdutos();
}

function limparFiltros() {
  document.getElementById("search-input").value = "";
  document.getElementById("filter-categoria").value = "";
  document.getElementById("filter-estoque").value = "";
  document.getElementById("filter-preco").value = "";
  document.getElementById("filter-ordem").value = "nome";
  
  produtosFiltrados = [...produtos];
  aplicarFiltros();
}

async function salvarProduto() {
    const id = document.getElementById("produto-id").value;
    const nome = document.getElementById("nome").value;
    const preco = parseFloat(document.getElementById("preco").value);
    const categoria = document.getElementById("categoria").value;
    const estoque = parseInt(document.getElementById("estoque").value);
    const data = document.getElementById("data").value;
    const descricao = document.getElementById("descricao").value;
    const imagemUrl = document.getElementById("imagemUrl").value || "https://via.placeholder.com/300x200?text=Produto+Sem+Imagem";

    // Validação
    if (!nome || isNaN(preco) || !categoria || isNaN(estoque) || !data) {
        showNotification('Por favor, preencha todos os campos obrigatórios.', 'error');
        return;
    }

    const produtoData = { 
        nome, 
        preco, 
        categoria, 
        estoque, 
        dataEntrada: data,
        descricao, 
        imagemUrl 
    };

    try {
        toggleButtonLoading(btnSalvar, true);
        
        let url;
        let method;
        
        if (id) {
            url = `${API_BASE_URL}/${id}`;
            method = 'PUT';
        } else {
            url = API_BASE_URL;
            method = 'POST';
        }
        
        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(produtoData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error('Erro do servidor:', response.status, errorText);
            
            try {
                const errorJson = JSON.parse(errorText);
                throw new Error(errorJson.mensagem || errorJson.message || `Erro ${response.status}: ${errorText}`);
            } catch (e) {
                throw new Error(`Erro ${response.status}: ${errorText}`);
            }
        }

        const resultado = await response.json();
        console.log('Resposta do servidor:', resultado);

        showNotification(id ? 'Produto atualizado com sucesso!' : 'Produto cadastrado com sucesso!', 'success');
        
        // Recarregar dados
        await carregarDados();
        
        // Limpar formulário e voltar para a lista
        if (!id) {
            limparFormulario();
        }
        
        showView('list');
        updateNavState('listar');
    } catch (error) {
        console.error('Erro completo:', error);
        showNotification('Erro ao salvar produto: ' + error.message, 'error');
    } finally {
        toggleButtonLoading(btnSalvar, false);
    }
}

async function editarProduto(id) {
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`);
    if (!response.ok) throw new Error('Erro ao carregar produto');
    const produto = await response.json();
    
    document.getElementById("produto-id").value = produto.id;
    document.getElementById("nome").value = produto.nome;
    document.getElementById("preco").value = produto.preco;
    document.getElementById("categoria").value = produto.categoria;
    document.getElementById("estoque").value = produto.estoque;
    document.getElementById("data").value = produto.dataEntrada;
    document.getElementById("descricao").value = produto.descricao || '';
    document.getElementById("imagemUrl").value = produto.imagemUrl || '';
    
    showView('form');
    updateNavState('cadastrar');
    
    window.scrollTo({ top: 0, behavior: 'smooth' });
  } catch (error) {
    console.error('Erro:', error);
    showNotification('Erro ao carregar produto', 'error');
  }
}

async function removerProduto(id) {
  if (!confirm('Tem certeza que deseja excluir este produto?')) {
    return;
  }
  
  try {
    const response = await fetch(`${API_BASE_URL}/${id}`, {
      method: 'DELETE'
    });
    
    if (!response.ok) throw new Error('Erro ao excluir produto');
    
    showNotification('Produto excluído com sucesso!', 'success');
    await carregarDados();
  } catch (error) {
    console.error('Erro:', error);
    showNotification('Erro ao excluir produto', 'error');
  }
}

async function atualizarDashboard() {
  try {
    const [totalProdutosRes, totalEstoqueRes, valorTotalRes] = await Promise.all([
      fetch(`${API_BASE_URL}/dashboard/total-produtos`),
      fetch(`${API_BASE_URL}/dashboard/total-estoque`),
      fetch(`${API_BASE_URL}/dashboard/valor-total`)
    ]);

    if (totalProdutosRes.ok) {
      const totalProdutos = await totalProdutosRes.text();
      document.getElementById("total-produtos").textContent = totalProdutos;
    }

    if (totalEstoqueRes.ok) {
      const totalEstoque = await totalEstoqueRes.text();
      document.getElementById("total-estoque").textContent = totalEstoque;
    }

    if (valorTotalRes.ok) {
      const valorTotal = await valorTotalRes.text();
      document.getElementById("valor-total").textContent = `R$ ${parseFloat(valorTotal).toFixed(2)}`;
    }
  } catch (error) {
    console.error('Erro ao carregar dashboard:', error);
  }
}

// Funções de view
function showView(view) {
  currentView = view;
  
  dashboardView.style.display = 'none';
  formView.style.display = 'none';
  listView.style.display = 'none';
  
  if (view === 'dashboard') {
    dashboardView.style.display = 'block';
    atualizarDashboard();
  } else if (view === 'form') {
    formView.style.display = 'block';
  } else if (view === 'list') {
    listView.style.display = 'flex';
    aplicarFiltros();
  }
}

function updateNavState(active) {
  navDashboard.classList.remove('active');
  navCadastrar.classList.remove('active');
  navListar.classList.remove('active');
  
  if (active === 'dashboard') {
    navDashboard.classList.add('active');
  } else if (active === 'cadastrar') {
    navCadastrar.classList.add('active');
  } else {
    navListar.classList.add('active');
  }
}

function renderizarProdutos() {
  const lista = document.getElementById("listaProdutos");
  
  if (produtosFiltrados.length === 0) {
    lista.innerHTML = `
      <div class="empty-state">
        <i class="fas fa-search"></i>
        <h2>Nenhum produto encontrado</h2>
        <p>Tente ajustar os filtros de busca ou cadastrar um novo produto.</p>
      </div>
    `;
    return;
  }
  
  lista.innerHTML = "";
  produtosFiltrados.forEach(produto => {
    const estoqueClass = produto.estoque > 0 ? (produto.estoque < 10 ? 'warning' : 'success') : 'danger';
    const estoqueText = produto.estoque > 0 ? `${produto.estoque} em estoque` : 'Fora de estoque';
    
    lista.innerHTML += `
      <div class="card">
        <img src="${produto.imagemUrl}" alt="${produto.nome}" onerror="this.src='https://via.placeholder.com/300x200?text=Imagem+Não+Encontrada'">
        <div class="card-content">
          <span class="category">${formatCategoria(produto.categoria)}</span>
          <h3>${produto.nome}</h3>
          <p>${produto.descricao || 'Sem descrição'}</p>
          <div class="price">R$ ${produto.preco.toFixed(2)}</div>
          
          <div class="card-footer">
            <span class="stock-info">${estoqueText}</span>
            <span class="date-info">${formatData(produto.dataEntrada)}</span>
          </div>
          
          <div class="card-actions">
            <button class="edit" onclick="editarProduto(${produto.id})">
              <i class="fas fa-edit"></i> Editar
            </button>
            <button class="delete" onclick="removerProduto(${produto.id})">
              <i class="fas fa-trash"></i> Excluir
            </button>
          </div>
        </div>
      </div>
    `;
  });
}

function limparFormulario() {
  document.getElementById("produto-id").value = "";
  document.getElementById("nome").value = "";
  document.getElementById("preco").value = "";
  document.getElementById("categoria").value = "";
  document.getElementById("estoque").value = "";
  document.getElementById("data").valueAsDate = new Date();
  document.getElementById("descricao").value = "";
  document.getElementById("imagemUrl").value = "";
}

function formatCategoria(categoria) {
  const categorias = {
    'ELETRONICOS': 'Eletrônicos',
    'LIVROS': 'Livros',
    'ROUPAS': 'Roupas',
    'ESPORTES': 'Esportes',
    'CASA': 'Casa e Cozinha'
  };
  
  return categorias[categoria] || categoria;
}

function formatData(dataString) {
  try {
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR');
  } catch (e) {
    return dataString;
  }
}

function showNotification(mensagem, tipo) {
  const notification = document.createElement('div');
  notification.className = `notification ${tipo}`;
  notification.innerHTML = `
    <i class="fas fa-${tipo === 'success' ? 'check-circle' : 'exclamation-circle'}"></i>
    ${mensagem}
  `;
  
  document.body.appendChild(notification);
  
  setTimeout(() => {
    notification.classList.add('show');
  }, 100);
  
  setTimeout(() => {
    notification.classList.remove('show');
    setTimeout(() => {
      document.body.removeChild(notification);
    }, 300);
  }, 3000);
}

function toggleLoading(show, message = '') {
  const lista = document.getElementById("listaProdutos");
  if (show) {
    lista.innerHTML = `
      <div class="empty-state">
        <i class="fas fa-spinner loading"></i>
        <h2>${message}</h2>
      </div>
    `;
  }
}

function toggleButtonLoading(button, loading) {
  if (loading) {
    button.innerHTML = '<div class="loading"></div> Processando...';
    button.disabled = true;
  } else {
    if (button.id === 'btn-salvar') {
      button.innerHTML = '<i class="fas fa-save"></i> Salvar Produto';
    } else if (button.id === 'btn-buscar') {
      button.innerHTML = '<i class="fas fa-search"></i> Buscar';
    }
    button.disabled = false;
  }
}

function fecharModal(modalId) {
  document.getElementById(modalId).style.display = 'none';
}

// Função para determinar a classe de estoque
function getStockClass(estoque) {
  if (estoque === 0) return 'none';
  if (estoque < 10) return 'low';
  return 'high';
}