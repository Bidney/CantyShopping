package io.github.bidney.cantyshopping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.bidney.cantyshopping.ui.theme.YourShoppingListTheme

class TermsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            YourShoppingListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TermsOfServiceScreen(
                        onBackPressed = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsOfServiceScreen(
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terms of Service") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Last Updated: ${getCurrentDate()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item { TermsSection(title = "1. Acceptance of Terms", content = acceptanceOfTerms) }
            item { TermsSection(title = "2. Description of Service", content = descriptionOfService) }
            item { TermsSection(title = "3. Privacy and Data Protection", content = privacyAndDataProtection) }
            item { TermsSection(title = "4. Age Requirements", content = ageRequirements) }
            item { TermsSection(title = "5. User Responsibilities", content = userResponsibilities) }
            item { TermsSection(title = "6. Intellectual Property", content = intellectualProperty) }
            item { TermsSection(title = "7. Service Availability", content = serviceAvailability) }
            item { TermsSection(title = "8. Disclaimers and Limitations", content = disclaimersAndLimitations) }
            item { TermsSection(title = "9. Indemnification", content = indemnification) }
            item { TermsSection(title = "10. Updates to Terms", content = updatesToTerms) }
            item { TermsSection(title = "11. Termination", content = termination) }
            item { TermsSection(title = "12. Governing Law and Disputes", content = governingLawAndDisputes) }
            item { TermsSection(title = "13. Severability", content = severability) }
            item { TermsSection(title = "14. Google Play Compliance", content = googlePlayCompliance) }
            item { TermsSection(title = "15. Accessibility", content = accessibility) }
            item { TermsSection(title = "16. Contact Information", content = contactInformation) }
            item { TermsSection(title = "17. Additional Consumer Rights", content = additionalConsumerRights) }
        }
    }
}

@Composable
fun TermsSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
        )
    }
}

fun getCurrentDate(): String {
    return java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault())
        .format(java.util.Date())
}

// Terms content
private const val acceptanceOfTerms = """
By downloading, installing, or using this grocery shopping list application ("App"), you agree to be bound by these Terms of Service and our Privacy Policy. If you do not agree, please do not use the App.
"""

private const val descriptionOfService = """
This App provides a simple grocery shopping list management service that allows users to:
• Create and manage shopping lists locally on your device
• Check off items as completed
• Delete lists when no longer needed
• Import and export lists in JSON format
"""

private const val privacyAndDataProtection = """
Your Privacy Rights:
• Your shopping lists are stored locally on your device only
• We do not collect, process, or store your personal data on our servers
• We do not track your usage or behavior
• No account registration or personal information is required
• When you export data, you control where it goes and with whom it's shared

Your Rights: You have the right to:
• Access your data (it's on your device)
• Delete your data (uninstall the app or clear app data)
• Export your data (using the export feature)
"""

private const val ageRequirements = """
You must be at least 13 years old to use this App. If you are between 13-18, you confirm you have parental consent. Users under 13 should not use this App.
"""

private const val userResponsibilities = """
You agree to:
• Use the App only for lawful purposes
• Not create or store illegal, harmful, threatening, or offensive content
• Not attempt to interfere with or disrupt the App's normal operation
• Maintain the security of your device and data

Note: If you have access to the source code under our open source license, those license terms govern your rights to modify and distribute the code.
"""

private const val intellectualProperty = """
The App's compiled version and branding are subject to intellectual property protections. The underlying source code may be available under separate open source licensing terms. You may use the App for personal purposes but may not copy the compiled app or redistribute it without permission, except as permitted by applicable open source licenses.
"""

private const val serviceAvailability = """
• The App is provided "as is" without guarantees of continuous availability
• We may update, modify, or discontinue features at any time
• We recommend using the export feature to back up important lists
"""

private const val disclaimersAndLimitations = """
To the fullest extent permitted by applicable law:
• We provide no warranties, express or implied
• We are not liable for data loss, device damage, or any indirect damages
• Our liability, if any, is limited to the amount you paid for the App (free apps = zero liability)
• Some jurisdictions don't allow liability exclusions, so these may not apply to you
"""

private const val indemnification = """
You agree to defend and hold us harmless from claims arising from your use of the App or violation of these terms.
"""

private const val updatesToTerms = """
We may update these Terms periodically. We'll notify users of material changes through the App or app store updates. Continued use after changes means acceptance.
"""

private const val termination = """
Either party may terminate this agreement at any time:
• You: by uninstalling the App
• Us: by discontinuing the App or for Terms violations
• Upon termination, these Terms survive where legally required
"""

private const val governingLawAndDisputes = """
Jurisdiction-Specific Provisions:
• These Terms are interpreted under the laws where you reside
• Disputes will be resolved in courts of competent jurisdiction in your location
• If your local laws provide greater consumer protections than these Terms, those local laws apply
• Nothing in these Terms limits your statutory consumer rights

Alternative Dispute Resolution:
Where permitted by law, we encourage resolving disputes through discussion before litigation.
"""

private const val severability = """
If any provision of these Terms is found invalid or unenforceable, the remaining provisions continue in effect.
"""

private const val googlePlayCompliance = """
This App complies with Google Play Developer Policy and Content Policy. Any violations should be reported through appropriate Google Play channels.
"""

private const val accessibility = """
We strive to make our App accessible to users with disabilities in accordance with applicable accessibility laws.
"""

private const val contactInformation = """
For questions about these Terms or privacy concerns, contact us at: cantylabs+tos@gmail.com

For technical support or app-related issues: cantylabs+tos@gmail.com
"""

private const val additionalConsumerRights = """
EU Users: You have rights under the General Data Protection Regulation (GDPR) and consumer protection laws.

California Users: You have rights under the California Consumer Privacy Act (CCPA) regarding personal information.

Other Jurisdictions: You retain all consumer and privacy rights granted by your local laws.

Nothing in these Terms waives or limits your statutory consumer protection rights.
"""