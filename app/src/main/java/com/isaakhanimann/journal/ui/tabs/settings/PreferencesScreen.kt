package com.isaakhanimann.journal.ui.tabs.settings

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.isaakhanimann.journal.localization.I18n
import com.isaakhanimann.journal.localization.i18n
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.DateLocaleOption
import com.isaakhanimann.journal.ui.utils.TimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    navigateBack: () -> Unit,
    navigateToIconPicker: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val supportedLanguages = remember(context) { I18n.getSupportedLanguages(context) }
    PreferencesScreen(
        navigateBack = navigateBack,
        navigateToIconPicker = navigateToIconPicker,
        supportedLanguages = supportedLanguages,
        selectedLanguageKey = viewModel.selectedLanguageFlow.collectAsState().value,
        saveSelectedLanguage = viewModel::saveSelectedLanguage,
        use24HourClock = viewModel.use24HourClockFlow.collectAsState().value
            ?: TimeFormat.systemDefaultIs24Hour(),
        saveUse24HourClock = viewModel::saveUse24HourClock,
        areDosageDotsHidden = viewModel.areDosageDotsHiddenFlow.collectAsState().value,
        saveDosageDotsAreHidden = viewModel::saveDosageDotsAreHidden,
        isOpenLinkInBrowser = viewModel.isOpenLinkInBrowserFlow.collectAsState().value,
        saveOpenLinkInBrowser = viewModel::saveOpenLinkInBrowser,
        isTimelineHidden = viewModel.isTimelineHiddenFlow.collectAsState().value,
        saveIsTimelineHidden = viewModel::saveIsTimelineHidden,
        areSubstanceHeightsIndependent =
            viewModel.areSubstanceHeightsIndependentFlow.collectAsState().value,
        saveAreSubstanceHeightsIndependent = viewModel::saveAreSubstanceHeightsIndependent,
        isMidnightCutoffEnabled = viewModel.isMidnightCutoffEnabledFlow.collectAsState().value,
        saveMidnightCutoffEnabled = viewModel::saveMidnightCutoffEnabled,
        isStatsByIngestionTime = viewModel.isStatsByIngestionTimeFlow.collectAsState().value,
        saveStatsByIngestionTime = viewModel::saveStatsByIngestionTime,
        dateLocaleOption = viewModel.dateLocaleOptionFlow.collectAsState().value,
        saveDateLocaleOption = viewModel::saveDateLocaleOption,
        isAppLockEnabled = viewModel.isAppLockEnabledFlow.collectAsState().value,
        saveAppLockEnabled = viewModel::saveAppLockEnabled,
        isEffectNotificationEnabled =
            viewModel.isEffectNotificationEnabledFlow.collectAsState().value,
        saveEffectNotificationEnabled = viewModel::saveEffectNotificationEnabled,
        visibleTabRoutes = viewModel.visibleTabRoutesFlow.collectAsState().value,
        saveTabVisible = viewModel::saveTabVisible
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    navigateBack: () -> Unit,
    navigateToIconPicker: () -> Unit,
    supportedLanguages: Map<String, String>,
    selectedLanguageKey: String?,
    saveSelectedLanguage: (String?) -> Unit,
    use24HourClock: Boolean,
    saveUse24HourClock: (Boolean) -> Unit,
    areDosageDotsHidden: Boolean,
    saveDosageDotsAreHidden: (Boolean) -> Unit,
    isOpenLinkInBrowser: Boolean,
    saveOpenLinkInBrowser: (Boolean) -> Unit,
    isTimelineHidden: Boolean,
    saveIsTimelineHidden: (Boolean) -> Unit,
    areSubstanceHeightsIndependent: Boolean,
    saveAreSubstanceHeightsIndependent: (Boolean) -> Unit,
    isMidnightCutoffEnabled: Boolean,
    saveMidnightCutoffEnabled: (Boolean) -> Unit,
    isStatsByIngestionTime: Boolean,
    saveStatsByIngestionTime: (Boolean) -> Unit,
    dateLocaleOption: DateLocaleOption,
    saveDateLocaleOption: (DateLocaleOption) -> Unit,
    isAppLockEnabled: Boolean,
    saveAppLockEnabled: (Boolean) -> Unit,
    isEffectNotificationEnabled: Boolean,
    saveEffectNotificationEnabled: (Boolean) -> Unit,
    visibleTabRoutes: Set<String> = TabVisibilityOption.entries.map { it.route }.toSet(),
    saveTabVisible: (String, Boolean) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(i18n("settings_preferences")) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = i18n("common_back")
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = horizontalPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CardWithTitle(title = i18n("settings_appearance"), innerPaddingHorizontal = 0.dp) {
                var isLanguageDialogVisible by remember { mutableStateOf(false) }
                val languageName =
                    supportedLanguages[selectedLanguageKey] ?: i18n("settings_language_system")
                SettingsButton(
                    imageVector = Icons.Outlined.Language,
                    text = i18n(
                        "settings_language_with_value",
                        mapOf("language" to languageName)
                    )
                ) {
                    isLanguageDialogVisible = true
                }
                if (isLanguageDialogVisible) {
                    LanguageSelectionDialog(
                        supportedLanguages = supportedLanguages,
                        selectedLanguageKey = selectedLanguageKey,
                        onSelectLanguage = {
                            saveSelectedLanguage(it)
                            I18n.setPreferredLanguageKey(it)
                            isLanguageDialogVisible = false
                        },
                        onDismiss = { isLanguageDialogVisible = false }
                    )
                }
                HorizontalDivider()
                SettingsButton(
                    imageVector = Icons.Outlined.StarBorder,
                    text = i18n("settings_icon_title")
                ) {
                    navigateToIconPicker()
                }
                HorizontalDivider()
                var isDateLocaleDialogVisible by remember { mutableStateOf(false) }
                SettingsButton(
                    imageVector = Icons.Outlined.Language,
                    text = i18n(
                        "settings_date_locale_with_value",
                        mapOf("value" to dateLocaleOptionLabel(dateLocaleOption))
                    )
                ) {
                    isDateLocaleDialogVisible = true
                }
                if (isDateLocaleDialogVisible) {
                    DateLocaleSelectionDialog(
                        selected = dateLocaleOption,
                        onSelect = {
                            saveDateLocaleOption(it)
                            isDateLocaleDialogVisible = false
                        },
                        onDismiss = { isDateLocaleDialogVisible = false }
                    )
                }
            }

            CardWithTitle(title = i18n("settings_ui"), innerPaddingHorizontal = 0.dp) {
                VisibleTabsRow(
                    visibleTabRoutes = visibleTabRoutes,
                    saveTabVisible = saveTabVisible
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_use_24_hour_clock"),
                    checked = use24HourClock,
                    onCheckedChange = saveUse24HourClock
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_hide_dosage_dots"),
                    checked = areDosageDotsHidden,
                    onCheckedChange = saveDosageDotsAreHidden
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_OpenLinkInBrowser"),
                    checked = isOpenLinkInBrowser,
                    onCheckedChange = saveOpenLinkInBrowser
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_hide_timeline"),
                    checked = isTimelineHidden,
                    onCheckedChange = saveIsTimelineHidden
                )
                HorizontalDivider()
                IndependentHeightsRow(
                    checked = areSubstanceHeightsIndependent,
                    onCheckedChange = saveAreSubstanceHeightsIndependent
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_midnight_cutoff"),
                    description = i18n("settings_midnight_cutoff_description"),
                    checked = isMidnightCutoffEnabled,
                    onCheckedChange = saveMidnightCutoffEnabled
                )
                HorizontalDivider()
                PreferenceSwitchRow(
                    title = i18n("settings_stats_by_ingestion_time"),
                    description = i18n("settings_stats_by_ingestion_time_description"),
                    checked = isStatsByIngestionTime,
                    onCheckedChange = saveStatsByIngestionTime
                )
            }

            CardWithTitle(title = i18n("settings_privacy"), innerPaddingHorizontal = 0.dp) {
                PreferenceSwitchRow(
                    title = i18n("settings_app_lock"),
                    description = i18n("settings_app_lock_description"),
                    checked = isAppLockEnabled,
                    onCheckedChange = saveAppLockEnabled
                )
                HorizontalDivider()
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (!granted) {
                        saveEffectNotificationEnabled(false)
                    }
                }
                PreferenceSwitchRow(
                    title = i18n("settings_effect_notification"),
                    description = i18n("settings_effect_notification_description"),
                    checked = isEffectNotificationEnabled,
                    onCheckedChange = { enabled ->
                        saveEffectNotificationEnabled(enabled)
                        if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Mirrors [com.isaakhanimann.journal.ui.main.navigation.routers.TabRouter]'s five
 * bottom-nav destinations. Routes are duplicated here as string literals (rather than
 * importing TabRouter) to avoid a settings-UI -> navigation dependency; they must stay
 * in sync with TabRouter's `route` values.
 */
private enum class TabVisibilityOption(val route: String, val labelKey: String, val icon: ImageVector) {
    JOURNAL(route = "journalTab", labelKey = "journal", icon = Icons.Filled.Book),
    STATISTICS(route = "statisticsTab", labelKey = "stats", icon = Icons.Filled.BarChart),
    SUBSTANCES(route = "substancesTab", labelKey = "substances", icon = Icons.Filled.Medication),
    SAFER_USE(route = "saferTab", labelKey = "safer", icon = Icons.Filled.HealthAndSafety),
    SETTINGS(route = "settingsTab", labelKey = "settings", icon = Icons.Filled.Settings)
}

@Composable
private fun VisibleTabsRow(
    visibleTabRoutes: Set<String>,
    saveTabVisible: (String, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp)
    ) {
        Text(text = i18n("settings_visible_tabs"))
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = i18n("settings_visible_tabs_description"),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabVisibilityOption.entries.forEach { option ->
                val isVisible = option.route in visibleTabRoutes
                // Prevent hiding the last remaining visible tab: with none left there
                // would be no way to reach any screen, including back into Settings.
                val isOnlyVisibleTab = isVisible && visibleTabRoutes.size == 1
                TabVisibilityToggle(
                    icon = option.icon,
                    contentDescription = i18n(option.labelKey),
                    isVisible = isVisible,
                    enabled = !isOnlyVisibleTab,
                    onToggle = { saveTabVisible(option.route, !isVisible) }
                )
            }
        }
    }
}

@Composable
private fun TabVisibilityToggle(
    icon: ImageVector,
    contentDescription: String,
    isVisible: Boolean,
    enabled: Boolean,
    onToggle: () -> Unit
) {
    val backgroundColor = if (isVisible) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (isVisible) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        IconButton(
            onClick = onToggle,
            enabled = enabled,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.4f))
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (enabled) contentColor else contentColor.copy(alpha = 0.6f)
            )
        }
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = contentDescription,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PreferenceSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    description: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(text = title)
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IndependentHeightsRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier
                .weight(1f)
                .clickable { showBottomSheet = true }
                .padding(end = ButtonDefaults.IconSpacing)
        ) {
            Text(text = i18n("settings_independent_substance_heights"))
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    Text(
                        text = i18n("settings_independent_substance_heights_description")
                            .trimIndent(),
                        modifier = Modifier
                            .padding(horizontal = horizontalPadding)
                            .padding(bottom = 15.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }
            }
            Icon(Icons.Outlined.Info, contentDescription = i18n("common_show_more_info"))
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun LanguageSelectionDialog(
    supportedLanguages: Map<String, String>,
    selectedLanguageKey: String?,
    onSelectLanguage: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val sortedLanguages = supportedLanguages.entries.sortedBy { it.value }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(i18n("settings_language_title")) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LanguageOptionRow(
                    label = i18n("settings_language_system"),
                    isSelected = selectedLanguageKey == null,
                    onClick = { onSelectLanguage(null) }
                )
                sortedLanguages.forEach { (key, label) ->
                    LanguageOptionRow(
                        label = label,
                        isSelected = selectedLanguageKey == key,
                        onClick = { onSelectLanguage(key) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(i18n("common_close"))
            }
        }
    )
}

@Composable
private fun LanguageOptionRow(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .padding(horizontal = horizontalPadding)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onClick)
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(label)
    }
}

@Composable
private fun dateLocaleOptionLabel(option: DateLocaleOption): String = when (option) {
    DateLocaleOption.FOLLOW_LANGUAGE -> i18n("settings_date_locale_follow_language")
    DateLocaleOption.FOLLOW_SYSTEM -> i18n("settings_date_locale_follow_system")
    DateLocaleOption.EN_US -> i18n("settings_date_locale_en_us")
    DateLocaleOption.ZH_CN -> i18n("settings_date_locale_zh_cn")
    DateLocaleOption.ZH_TW -> i18n("settings_date_locale_zh_tw")
}

@Composable
private fun DateLocaleSelectionDialog(
    selected: DateLocaleOption,
    onSelect: (DateLocaleOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(i18n("settings_date_locale_title")) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                DateLocaleOption.entries.forEach { option ->
                    LanguageOptionRow(
                        label = dateLocaleOptionLabel(option),
                        isSelected = selected == option,
                        onClick = { onSelect(option) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(i18n("common_close"))
            }
        }
    )
}