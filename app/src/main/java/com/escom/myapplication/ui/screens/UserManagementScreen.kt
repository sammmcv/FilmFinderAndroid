package com.escom.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.escom.myapplication.model.User
import com.escom.myapplication.viewmodel.AuthViewModel

@Composable
fun UserManagementScreen(
    authViewModel: AuthViewModel,
    onBackPressed: () -> Unit
) {
    val users by authViewModel.users.observeAsState(emptyList())
    val loading by authViewModel.loading.observeAsState(false)
    val error by authViewModel.error.observeAsState()
    val operationMessage by authViewModel.operationMessage.observeAsState()
    val context = LocalContext.current
    
    // Load users when the screen is first displayed
    LaunchedEffect(Unit) {
        authViewModel.getAllUsers()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            
            Text(
                text = "Gestión de Usuarios",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Show loading indicator
        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Show user list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(users) { user ->
                    UserItem(
                        user = user,
                        onUpdateRole = { newRole ->
                            authViewModel.updateUserRole(user.id, newRole)
                        },
                        onDelete = {
                            authViewModel.deleteUser(user.id)
                        }
                    )
                }
            }
        }
        
        // Show errors if any
        error?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                authViewModel.clearError()
            }
        }
        
        // Show operation messages if any
        operationMessage?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                authViewModel.clearOperationMessage()
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
    onUpdateRole: (String) -> Unit,
    onDelete: () -> Unit
) {
    var showRoleDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Text(
                text = "Rol: ${user.role}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { showRoleDialog = true },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Text("Cambiar Rol")
                }
                
                Button(
                    onClick = { showDeleteConfirmation = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
    
    // Role selection dialog
    if (showRoleDialog) {
        AlertDialog(
            onDismissRequest = { showRoleDialog = false },
            title = { Text("Cambiar Rol") },
            text = { 
                Column {
                    Text("Selecciona el nuevo rol para ${user.firstName} ${user.lastName}")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { 
                            onUpdateRole("USER")
                            showRoleDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Usuario")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { 
                            onUpdateRole("ADMIN")
                            showRoleDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Administrador")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showRoleDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    
    // Delete confirmation dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${user.firstName} ${user.lastName}? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = { 
                        onDelete()
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}