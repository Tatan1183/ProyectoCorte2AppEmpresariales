// Variables globales
let currentModule = "citas"
const API_URL = "/api"
let currentEditId = null


document.addEventListener("DOMContentLoaded", () => {
    // --- CÓDIGO PARA BIENVENIDA ---
    try {
        const userString = localStorage.getItem('user');
        if (userString) {
            const user = JSON.parse(userString);
            const welcomeElement = document.getElementById('welcomeMessage');
            if (welcomeElement && user && user.nombre && user.apellido) {
                // Asumiendo 'Dr.' - podrías hacerlo más genérico
                welcomeElement.textContent = `Bienvenido/a Dr. ${user.nombre} ${user.apellido}`;
            }
        }
    } catch (e) {
        console.error("Error al parsear datos de usuario para bienvenida:", e);
        // No mostrar mensaje si hay error
    }
    // Cargar módulo inicial (tu código existente)
    loadModule("citas");
    checkAuth(); // Llama a checkAuth aquí o asegúrate que se llame antes
});




// Función para cargar un módulo específico
function loadModule(module) {
  currentModule = module
  document.getElementById("currentModuleTitle").textContent = capitalizeFirstLetter(module)

  // Configurar encabezados de tabla según el módulo
  setupTableHeaders(module)

  // Cargar datos
  fetchData(module)

  // Cargar datos relacionados para los selects
  loadRelatedData()
}

// Configurar encabezados de tabla según el módulo
function setupTableHeaders(module) {
  const tableHeaders = document.getElementById("tableHeaders")
  tableHeaders.innerHTML = ""

  // Columnas específicas por módulo
  let specificColumns = []

  switch (module) {
    case "citas":
      specificColumns = ["ID", "Mascota", "Veterinario", "Servicio", "Fecha y Hora", "Estado", "Acciones"]
      break
    case "clientes":
      specificColumns = ["ID", "Nombre", "Apellido", "Teléfono", "Email", "Dirección", "Acciones"]
      break
    case "mascotas":
      specificColumns = ["ID", "Nombre", "Especie", "Raza", "Fecha Nacimiento", "Dueño", "Imagen", "Acciones"]
      break
    case "servicios":
      specificColumns = ["ID", "Nombre", "Descripción", "Precio", "Acciones"]
      break
    case "veterinarios":
      specificColumns = ["ID", "Nombre", "Apellido", "Especialidad", "Email", "Imagen", "Acciones"]
      break
  }

  // Agregar columnas
  specificColumns.forEach((column) => {
    const th = document.createElement("th")
    th.textContent = column
    tableHeaders.appendChild(th)
  })
}

// Función para obtener datos del API
async function fetchData(module) {
  try {
    const response = await fetch(`${API_URL}/${module}`)
    if (!response.ok) {
      throw new Error(`Error al cargar ${module}: ${response.statusText}`)
    }

    const data = await response.json()
    renderTable(data, module)
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al cargar datos: ${error.message}`)
  }
}

// Función para renderizar la tabla con los datos
function renderTable(data, module) {
  const tableBody = document.getElementById("tableBody")
  tableBody.innerHTML = ""

  if (data.length === 0) {
    const emptyRow = document.createElement("tr")
    const emptyCell = document.createElement("td")
    emptyCell.colSpan = 10
    emptyCell.textContent = `No hay ${module} registrados`
    emptyCell.className = "text-center"
    emptyRow.appendChild(emptyCell)
    tableBody.appendChild(emptyRow)
    return
  }

  data.forEach((item) => {
    const row = document.createElement("tr")

    // Columnas específicas según el módulo
    switch (module) {
      case "citas":
        addCell(row, item.id)
        addCell(row, item.mascotaNombre || "N/A")
        addCell(row, item.veterinarioNombre ? `${item.veterinarioNombre} ${item.veterinarioApellido}` : "N/A")
        addCell(row, item.servicioNombre || "N/A")
        addCell(row, formatDateTime(item.fechaHora))
        addCell(row, item.estado)
        break
      case "clientes":
        addCell(row, item.id)
        addCell(row, item.nombre)
        addCell(row, item.apellido)
        addCell(row, item.telefono)
        addCell(row, item.email)
        addCell(row, item.direccion)
        break
      case "mascotas":
        addCell(row, item.id)
        addCell(row, item.nombre)
        addCell(row, item.especie)
        addCell(row, item.raza)
        addCell(row, formatDate(item.fechaNacimiento))
        addCell(row, item.clienteNombre ? `${item.clienteNombre} ${item.clienteApellido}` : "N/A")

        // Celda de imagen
        const imgCell = document.createElement("td")
        if (item.imagen) {
          const img = document.createElement("img")
          img.src = `/images/${item.imagen}`
          img.alt = item.nombre
          img.className = "img-thumbnail"
          imgCell.appendChild(img)
        } else {
          imgCell.textContent = "Sin imagen"
        }
        row.appendChild(imgCell)
        break
      case "servicios":
        addCell(row, item.id)
        addCell(row, item.nombre)
        addCell(row, item.descripcion)
        addCell(row, `${item.precio.toFixed(2)}`)
        break
      case "veterinarios":
        addCell(row, item.id)
        addCell(row, item.nombre)
        addCell(row, item.apellido)
        addCell(row, item.especialidad)
        addCell(row, item.email)

        // Celda de imagen
        const vetImgCell = document.createElement("td")
        if (item.imagen) {
          const img = document.createElement("img")
          img.src = `/images/${item.imagen}`
          img.alt = item.nombre
          img.className = "img-thumbnail"
          vetImgCell.appendChild(img)
        } else {
          vetImgCell.textContent = "Sin imagen"
        }
        row.appendChild(vetImgCell)
        break
    }

    // Columna de acciones
    const actionsCell = document.createElement("td")

    // Botón editar
    const editBtn = document.createElement("button")
    editBtn.className = "btn btn-sm color1 me-2"
    editBtn.innerHTML = '<i class="bi bi-pencil-square"></i>'
    editBtn.onclick = () => showEditModal(item.id)
    actionsCell.appendChild(editBtn)

    // Botón eliminar
    const deleteBtn = document.createElement("button")
    deleteBtn.className = "btn btn-sm color3"
    deleteBtn.innerHTML = '<i class="bi bi-trash"></i>'
    deleteBtn.onclick = () => deleteItem(item.id)
    actionsCell.appendChild(deleteBtn)

    row.appendChild(actionsCell)
    tableBody.appendChild(row)
  })
}

// Función auxiliar para agregar una celda a una fila
function addCell(row, text) {
  const cell = document.createElement("td")
  cell.textContent = text
  row.appendChild(cell)
}

// Función para mostrar el modal de agregar
function showAddModal() {
  currentEditId = null

  // Limpiar formulario
  clearForm()

  // Configurar título del modal
  const modalTitle = document.getElementById(`${currentModule.slice(0, -1)}ModalTitle`)
  modalTitle.textContent = `Registrar Nuevo ${capitalizeFirstLetter(currentModule.slice(0, -1))}`

  // Mostrar modal
  const modalId = `modal${capitalizeFirstLetter(currentModule.slice(0, -1))}`
  const modalElement = document.getElementById(modalId)
  const modal = new bootstrap.Modal(modalElement)
  modal.show()
}

// Función para mostrar el modal de editar
async function showEditModal(id) {
  currentEditId = id

  try {
    const response = await fetch(`${API_URL}/${currentModule}/${id}`)
    if (!response.ok) {
      throw new Error(`Error al cargar datos: ${response.statusText}`)
    }

    const item = await response.json()

    // Configurar título del modal
    const modalTitle = document.getElementById(`${currentModule.slice(0, -1)}ModalTitle`)
    modalTitle.textContent = `Editar ${capitalizeFirstLetter(currentModule.slice(0, -1))}`

    // Llenar formulario con datos
    fillForm(item)

    // Mostrar modal
    const modalId = `modal${capitalizeFirstLetter(currentModule.slice(0, -1))}`
    const modalElement = document.getElementById(modalId)
    const modal = new bootstrap.Modal(modalElement)
    modal.show()
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al cargar datos: ${error.message}`)
  }
}

// Función para llenar el formulario con datos
function fillForm(item) {
  // Establecer ID oculto
  document.getElementById(`${currentModule.slice(0, -1)}Id`).value = item.id

  switch (currentModule) {
    case "citas":
      if (item.mascotaId) {
        document.getElementById("mascotaSelect").value = item.mascotaId
      }
      if (item.veterinarioId) {
        document.getElementById("veterinarioSelect").value = item.veterinarioId
      }
      if (item.servicioId) {
        document.getElementById("servicioSelect").value = item.servicioId
      }
      document.getElementById("fechaHora").value = formatDateTimeForInput(item.fechaHora)
      document.getElementById("estado").value = item.estado
      document.getElementById("notas").value = item.notas || ""
      break
    case "clientes":
      document.getElementById("nombreCliente").value = item.nombre
      document.getElementById("apellidoCliente").value = item.apellido
      document.getElementById("telefonoCliente").value = item.telefono
      document.getElementById("emailCliente").value = item.email
      document.getElementById("direccionCliente").value = item.direccion
      break
    case "mascotas":
      document.getElementById("nombreMascota").value = item.nombre
      document.getElementById("especieMascota").value = item.especie
      document.getElementById("razaMascota").value = item.raza
      document.getElementById("fechaNacimientoMascota").value = formatDateForInput(item.fechaNacimiento)
      if (item.clienteId) {
        document.getElementById("clienteMascota").value = item.clienteId
      }
      // No se puede establecer el valor del input file
      break
    case "servicios":
      document.getElementById("nombreServicio").value = item.nombre
      document.getElementById("descripcionServicio").value = item.descripcion
      document.getElementById("precioServicio").value = item.precio
      break
    case "veterinarios":
      document.getElementById("nombreVeterinario").value = item.nombre
      document.getElementById("apellidoVeterinario").value = item.apellido
      document.getElementById("especialidadVeterinario").value = item.especialidad
      document.getElementById("emailVeterinario").value = item.email
      // No establecemos la contraseña por seguridad
      // No se puede establecer el valor del input file
      break
  }
}

// Función para limpiar el formulario
function clearForm() {
  // Limpiar ID oculto
  const idInput = document.getElementById(`${currentModule.slice(0, -1)}Id`)
  if (idInput) idInput.value = ""

  switch (currentModule) {
    case "citas":
      document.getElementById("mascotaSelect").value = ""
      document.getElementById("veterinarioSelect").value = ""
      document.getElementById("servicioSelect").value = ""
      document.getElementById("fechaHora").value = ""
      document.getElementById("estado").value = ""
      document.getElementById("notas").value = ""
      break
    case "clientes":
      document.getElementById("nombreCliente").value = ""
      document.getElementById("apellidoCliente").value = ""
      document.getElementById("telefonoCliente").value = ""
      document.getElementById("emailCliente").value = ""
      document.getElementById("direccionCliente").value = ""
      break
    case "mascotas":
      document.getElementById("nombreMascota").value = ""
      document.getElementById("especieMascota").value = ""
      document.getElementById("razaMascota").value = ""
      document.getElementById("fechaNacimientoMascota").value = ""
      document.getElementById("clienteMascota").value = ""
      document.getElementById("imagenMascota").value = ""
      break
    case "servicios":
      document.getElementById("nombreServicio").value = ""
      document.getElementById("descripcionServicio").value = ""
      document.getElementById("precioServicio").value = ""
      break
    case "veterinarios":
      document.getElementById("nombreVeterinario").value = ""
      document.getElementById("apellidoVeterinario").value = ""
      document.getElementById("especialidadVeterinario").value = ""
      document.getElementById("emailVeterinario").value = ""
      document.getElementById("passwordVeterinario").value = ""
      document.getElementById("imagenVeterinario").value = ""
      break
  }
}

// Función para guardar un elemento
async function saveItem() {
  switch (currentModule) {
    case "citas":
      saveCita()
      break
    case "clientes":
      saveCliente()
      break
    case "mascotas":
      saveMascota()
      break
    case "servicios":
      saveServicio()
      break
    case "veterinarios":
      saveVeterinario()
      break
  }
}

// Función para guardar una cita
async function saveCita() {
  const mascotaId = document.getElementById("mascotaSelect").value
  const veterinarioId = document.getElementById("veterinarioSelect").value
  const servicioId = document.getElementById("servicioSelect").value
  const fechaHora = document.getElementById("fechaHora").value
  const estado = document.getElementById("estado").value
  const notas = document.getElementById("notas").value

  if (!mascotaId || !veterinarioId || !servicioId || !fechaHora || !estado) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  const cita = {
    mascotaId: Number.parseInt(mascotaId),
    veterinarioId: Number.parseInt(veterinarioId),
    servicioId: Number.parseInt(servicioId),
    fechaHora: fechaHora,
    estado: estado,
    notas: notas,
  }

  if (currentEditId) {
    cita.id = currentEditId
  }

  try {
    const method = currentEditId ? "PUT" : "POST"
    const url = currentEditId ? `${API_URL}/citas/${currentEditId}` : `${API_URL}/citas`

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(cita),
    })

    if (!response.ok) {
      throw new Error(`Error al guardar cita: ${response.statusText}`)
    }

    // Cerrar modal
    const modalElement = document.getElementById("modalCita")
    const modal = bootstrap.Modal.getInstance(modalElement)
    modal.hide()

    // Recargar datos
    fetchData("citas")

    alert(currentEditId ? "Cita actualizada correctamente" : "Cita registrada correctamente")
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al guardar cita: ${error.message}`)
  }
}

// Función para guardar un cliente
async function saveCliente() {
  const nombre = document.getElementById("nombreCliente").value
  const apellido = document.getElementById("apellidoCliente").value
  const telefono = document.getElementById("telefonoCliente").value
  const email = document.getElementById("emailCliente").value
  const direccion = document.getElementById("direccionCliente").value

  if (!nombre || !apellido || !telefono || !email || !direccion) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  const cliente = {
    nombre: nombre,
    apellido: apellido,
    telefono: telefono,
    email: email,
    direccion: direccion,
  }

  if (currentEditId) {
    cliente.id = currentEditId
  }

  try {
    const method = currentEditId ? "PUT" : "POST"
    const url = currentEditId ? `${API_URL}/clientes/${currentEditId}` : `${API_URL}/clientes`

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(cliente),
    })

    if (!response.ok) {
      throw new Error(`Error al guardar cliente: ${response.statusText}`)
    }

    // Cerrar modal
    const modalElement = document.getElementById("modalCliente")
    const modal = bootstrap.Modal.getInstance(modalElement)
    modal.hide()

    // Recargar datos
    fetchData("clientes")

    alert(currentEditId ? "Cliente actualizado correctamente" : "Cliente registrado correctamente")
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al guardar cliente: ${error.message}`)
  }
}

// Función para guardar una mascota
async function saveMascota() {
  const nombre = document.getElementById("nombreMascota").value
  const especie = document.getElementById("especieMascota").value
  const raza = document.getElementById("razaMascota").value
  const fechaNacimiento = document.getElementById("fechaNacimientoMascota").value
  const clienteId = document.getElementById("clienteMascota").value
  const imagenInput = document.getElementById("imagenMascota")

  if (!nombre || !especie || !raza || !fechaNacimiento || !clienteId) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  let imagenNombre = null

  // Si hay una imagen seleccionada, subirla primero
  if (imagenInput.files.length > 0) {
    try {
      const formData = new FormData()
      formData.append("file", imagenInput.files[0])

      const uploadResponse = await fetch(`${API_URL}/mascotas/upload`, {
        method: "POST",
        body: formData,
      })

      if (!uploadResponse.ok) {
        throw new Error(`Error al subir imagen: ${uploadResponse.statusText}`)
      }

      imagenNombre = await uploadResponse.text()
    } catch (error) {
      console.error("Error al subir imagen:", error)
      alert(`Error al subir imagen: ${error.message}`)
      return
    }
  }

  const mascota = {
    nombre: nombre,
    especie: especie,
    raza: raza,
    fechaNacimiento: fechaNacimiento,
    clienteId: Number.parseInt(clienteId),
  }

  if (imagenNombre) {
    mascota.imagen = imagenNombre
  }

  if (currentEditId) {
    mascota.id = currentEditId
  }

  try {
    const method = currentEditId ? "PUT" : "POST"
    const url = currentEditId ? `${API_URL}/mascotas/${currentEditId}` : `${API_URL}/mascotas`

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(mascota),
    })

    if (!response.ok) {
      throw new Error(`Error al guardar mascota: ${response.statusText}`)
    }

    // Cerrar modal
    const modalElement = document.getElementById("modalMascota")
    const modal = bootstrap.Modal.getInstance(modalElement)
    modal.hide()

    // Recargar datos
    fetchData("mascotas")

    alert(currentEditId ? "Mascota actualizada correctamente" : "Mascota registrada correctamente")
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al guardar mascota: ${error.message}`)
  }
}

// Función para guardar un servicio
async function saveServicio() {
  const nombre = document.getElementById("nombreServicio").value
  const descripcion = document.getElementById("descripcionServicio").value
  const precio = document.getElementById("precioServicio").value

  if (!nombre || !descripcion || !precio) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  const servicio = {
    nombre: nombre,
    descripcion: descripcion,
    precio: Number.parseFloat(precio),
  }

  if (currentEditId) {
    servicio.id = currentEditId
  }

  try {
    const method = currentEditId ? "PUT" : "POST"
    const url = currentEditId ? `${API_URL}/servicios/${currentEditId}` : `${API_URL}/servicios`

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(servicio),
    })

    if (!response.ok) {
      throw new Error(`Error al guardar servicio: ${response.statusText}`)
    }

    // Cerrar modal
    const modalElement = document.getElementById("modalServicio")
    const modal = bootstrap.Modal.getInstance(modalElement)
    modal.hide()

    // Recargar datos
    fetchData("servicios")

    alert(currentEditId ? "Servicio actualizado correctamente" : "Servicio registrado correctamente")
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al guardar servicio: ${error.message}`)
  }
}

// Función para guardar un veterinario
async function saveVeterinario() {
  const nombre = document.getElementById("nombreVeterinario").value
  const apellido = document.getElementById("apellidoVeterinario").value
  const especialidad = document.getElementById("especialidadVeterinario").value
  const email = document.getElementById("emailVeterinario").value
  const password = document.getElementById("passwordVeterinario").value
  const imagenInput = document.getElementById("imagenVeterinario")

  if (!nombre || !apellido || !especialidad || !email) {
    alert("Por favor complete todos los campos obligatorios")
    return
  }

  // Si es nuevo registro, la contraseña es obligatoria
  if (!currentEditId && !password) {
    alert("Por favor ingrese una contraseña")
    return
  }

  let imagenNombre = null

  // Si hay una imagen seleccionada, subirla primero
  if (imagenInput.files.length > 0) {
    try {
      const formData = new FormData()
      formData.append("file", imagenInput.files[0])

      const uploadResponse = await fetch(`${API_URL}/veterinarios/upload`, {
        method: "POST",
        body: formData,
      })

      if (!uploadResponse.ok) {
        throw new Error(`Error al subir imagen: ${uploadResponse.statusText}`)
      }

      imagenNombre = await uploadResponse.text()
    } catch (error) {
      console.error("Error al subir imagen:", error)
      alert(`Error al subir imagen: ${error.message}`)
      return
    }
  }

  const veterinario = {
    nombre: nombre,
    apellido: apellido,
    especialidad: especialidad,
    email: email,
  }

  if (password) {
    veterinario.password = password
  }

  if (imagenNombre) {
    veterinario.imagen = imagenNombre
  }

  if (currentEditId) {
    veterinario.id = currentEditId
  }

  try {
    const method = currentEditId ? "PUT" : "POST"
    const url = currentEditId ? `${API_URL}/veterinarios/${currentEditId}` : `${API_URL}/veterinarios`

    const response = await fetch(url, {
      method: method,
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(veterinario),
    })

    if (!response.ok) {
      throw new Error(`Error al guardar veterinario: ${response.statusText}`)
    }

    // Cerrar modal
    const modalElement = document.getElementById("modalVeterinario")
    const modal = bootstrap.Modal.getInstance(modalElement)
    modal.hide()

    // Recargar datos
    fetchData("veterinarios")

    alert(currentEditId ? "Veterinario actualizado correctamente" : "Veterinario registrado correctamente")
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al guardar veterinario: ${error.message}`)
  }
}

// Función para eliminar un elemento
async function deleteItem(id) {
  if (!confirm(`¿Está seguro de eliminar este ${currentModule.slice(0, -1)}?`)) {
    return
  }

  try {
    const response = await fetch(`${API_URL}/${currentModule}/${id}`, {
      method: "DELETE",
    })

    if (!response.ok) {
      throw new Error(`Error al eliminar: ${response.statusText}`)
    }

    // Recargar datos
    fetchData(currentModule)

    alert(`${capitalizeFirstLetter(currentModule.slice(0, -1))} eliminado correctamente`)
  } catch (error) {
    console.error("Error:", error)
    alert(`Error al eliminar: ${error.message}`)
  }
}

// Función para cargar datos relacionados para los selects
async function loadRelatedData() {
  switch (currentModule) {
    case "citas":
      loadMascotasForSelect()
      loadVeterinariosForSelect()
      loadServiciosForSelect()
      break
    case "mascotas":
      loadClientesForSelect()
      break
  }
}

// Función para cargar mascotas en select
async function loadMascotasForSelect() {
  try {
    const response = await fetch(`${API_URL}/mascotas`)
    if (!response.ok) {
      throw new Error(`Error al cargar mascotas: ${response.statusText}`)
    }

    const mascotas = await response.json()
    const select = document.getElementById("mascotaSelect")

    // Mantener la opción por defecto
    select.innerHTML = '<option value="">Seleccionar Mascota</option>'

    mascotas.forEach((mascota) => {
      const option = document.createElement("option")
      option.value = mascota.id
      option.textContent = `${mascota.nombre} (${mascota.especie})`
      select.appendChild(option)
    })
  } catch (error) {
    console.error("Error:", error)
  }
}

// Función para cargar veterinarios en select
async function loadVeterinariosForSelect() {
  try {
    const response = await fetch(`${API_URL}/veterinarios`)
    if (!response.ok) {
      throw new Error(`Error al cargar veterinarios: ${response.statusText}`)
    }

    const veterinarios = await response.json()
    const select = document.getElementById("veterinarioSelect")

    // Mantener la opción por defecto
    select.innerHTML = '<option value="">Seleccionar Veterinario</option>'

    veterinarios.forEach((veterinario) => {
      const option = document.createElement("option")
      option.value = veterinario.id
      option.textContent = `${veterinario.nombre} ${veterinario.apellido}`
      select.appendChild(option)
    })
  } catch (error) {
    console.error("Error:", error)
  }
}

// Función para cargar servicios en select
async function loadServiciosForSelect() {
  try {
    const response = await fetch(`${API_URL}/servicios`)
    if (!response.ok) {
      throw new Error(`Error al cargar servicios: ${response.statusText}`)
    }

    const servicios = await response.json()
    const select = document.getElementById("servicioSelect")

    // Mantener la opción por defecto
    select.innerHTML = '<option value="">Seleccionar Servicio</option>'

    servicios.forEach((servicio) => {
      const option = document.createElement("option")
      option.value = servicio.id
      option.textContent = `${servicio.nombre} (${servicio.precio.toFixed(2)})`
      select.appendChild(option)
    })
  } catch (error) {
    console.error("Error:", error)
  }
}

// Función para cargar clientes en select
async function loadClientesForSelect() {
  try {
    const response = await fetch(`${API_URL}/clientes`)
    if (!response.ok) {
      throw new Error(`Error al cargar clientes: ${response.statusText}`)
    }

    const clientes = await response.json()
    const select = document.getElementById("clienteMascota")

    // Mantener la opción por defecto
    select.innerHTML = '<option value="">Seleccionar Dueño</option>'

    clientes.forEach((cliente) => {
      const option = document.createElement("option")
      option.value = cliente.id
      option.textContent = `${cliente.nombre} ${cliente.apellido}`
      select.appendChild(option)
    })
  } catch (error) {
    console.error("Error:", error)
  }
}

// Funciones de utilidad
function capitalizeFirstLetter(string) {
  return string.charAt(0).toUpperCase() + string.slice(1)
}

function formatDate(dateString) {
  if (!dateString) return "N/A"
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

function formatDateTime(dateTimeString) {
  if (!dateTimeString) return "N/A"
  const date = new Date(dateTimeString)
  return date.toLocaleString()
}

function formatDateForInput(dateString) {
  if (!dateString) return ""
  const date = new Date(dateString)
  return date.toISOString().split("T")[0]
}

function formatDateTimeForInput(dateTimeString) {
  if (!dateTimeString) return ""
  const date = new Date(dateTimeString)
  return date.toISOString().slice(0, 16)
}
