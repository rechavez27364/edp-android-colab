package com.example.myapplication

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileForm(state: ProfileUiState, viewModel: ProfileViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Edit Profile",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        StandardTextField(state.name, { viewModel.onNameChange(it) }, "Full Name", Icons.Default.Person)
        StandardTextField(state.email, { viewModel.onEmailChange(it) }, "Email Address", Icons.Default.Email)
        StandardTextField(state.contactNumber, { viewModel.onContactChange(it) }, "Contact Number", Icons.Default.Phone)
        StandardTextField(state.address, { viewModel.onAddressChange(it) }, "Address", Icons.Default.Home)
        StandardTextField(state.username, { viewModel.onUsernameChange(it) }, "Username", Icons.Default.AccountCircle)

        Spacer(modifier = Modifier.height(32.dp))
        
        Text(text = "Skills", style = MaterialTheme.typography.titleMedium)
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = state.newSkill,
                onValueChange = { viewModel.onNewSkillChange(it) },
                label = { Text("Add a skill") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { viewModel.addSkill() },
                modifier = Modifier.height(56.dp)
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Displaying skills in a simple column with remove buttons
        state.skills.forEach { skill ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "• $skill")
                TextButton(onClick = { viewModel.removeSkill(skill) }) {
                    Text("Remove", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = { viewModel.showPreview() },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Show Preview")
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StandardTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        singleLine = true
    )
}

@Composable
fun ProfilePreview(state: ProfileUiState, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Profile Summary",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        InfoField("Name", state.name)
        InfoField("Email", state.email)
        InfoField("Contact", state.contactNumber)
        InfoField("Address", state.address)
        InfoField("Username", state.username)

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(text = "Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        if (state.skills.isEmpty()) {
            Text("None added")
        } else {
            state.skills.forEach { skill ->
                Text(text = "• $skill", modifier = Modifier.padding(vertical = 2.dp))
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Back to Edit")
        }
    }
}

@Composable
fun InfoField(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = if (value.isEmpty()) "Not provided" else value, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    
    Crossfade(targetState = state.isPreview, label = "Transition") { isPreview ->
        if (isPreview) {
            ProfilePreview(state = state, onBack = { viewModel.backToEdit() })
        } else {
            ProfileForm(state = state, viewModel = viewModel)
        }
    }
}
