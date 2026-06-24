package com.example

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIPromptHubScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("@builder", "@executor", "@treedoc", "القوالب")

    // Dynamic data matching the requirements
    val titleText = when (selectedTab) {
        0 -> "كيف تطلب من المساعد الذكي استخدام @builder؟"
        1 -> "كيف تطلب من المساعد الذكي إصدار أوامر @executor؟"
        2 -> "كيف تطلب من المساعد الذكي استعراض بنية المجلدات @treedoc؟"
        else -> "كيف تطلب من المساعد الذكي تصميم قوالب المشاريع؟"
    }

    val explanationText = when (selectedTab) {
        0 -> "يستخدم وسم @builder لإنشاء الملفات البرمجية أو تحديثها وتعديلها تلقائياً بالكامل في المجلد النشط عبر المنصة."
        1 -> "يسمح وسم @executor للمساعد الخارجي بإصدار أوامر إدارة ملفات مباشرة، مثل إنشاء المجلدات، النقل، الحذف، والنسخ الاحتياطي."
        2 -> "يساعد أمر @treedoc المساعد في طلب تقارير شجرية أو مسح هيكلي للتحقق من سلامة بناء الملفات والمجلدات الحالية مجدولاً."
        else -> "يوضح هذا القسم الهيكل البرمجي المعتمد لقوالب المشاريع بصيغة JSON لتصميم قوالب كاملة للمشروع وبنائها فوراً."
    }

    val systemPromptText = when (selectedTab) {
        0 -> """
            أنت مساعد برمجيات ذكي وخبير في المنصة الذكية. يمكنك إنشاء ملفات جديدة داخل المشروع النشط أو تحديثها برمجياً بنجاح عبر صياغة المحتويات وتمريرها داخل كتل وسم @builder المعياري.
            
            يتم فحص وقراءة كتل @builder تلقائياً عند حفظ النص أو التقاطه بالحافظة.
            تنسيق وسم البناء المعتمد للملفات يجب أن يكون كالتالي:
            
            [بادئة التعليق لكل لغة] @builder:file اسم_الملف_مع_المسار_النسبي.الامتداد
            [اكتب محتوى الملف الكامل أو المحدث هنا برصانة وتفصيل]
            [بادئة التعليق لكل لغة] @builder:end
            
            مثال على تعليق لغة بايثون:
            # @builder:file main.py
            # print("hello world")
            # @builder:end
            
            تأكد من تطابق بادئات التعليق حسب لغة الملف، وكتابة الكود كاملاً دون حذف أو اختصار لتجنب المشاكل في البناء.
        """.trimIndent()

        1 -> """
            أنت منفذ أوامر معتمد ذو صلاحيات متقدمة داخل سياق المنصة الذكية. يمكنك تفعيل وإدارة الملفات والمجلدات عبر تمرير الأوامر الهيكلية برفق باستخدام بادئة @executor في سطر مستقل تماماً.
            
            الأوامر المدعومة من المحرك المنفذ هي:
            - @executor:mkdir --path=اسم_المجلد : لإنشاء مجلد فرعي جديد.
            - @executor:move --path=الملف_المراد_نقله --dest=المجلد_المستهدف : لنقل الملفات.
            - @executor:rename --path=المسار_الحالي --newName=الاسم_الجديد : لإعادة مسمى ملف أو مجلد.
            - @executor:delete --path=المسار_المستهدف : لحذف الملف أو المجلد نهائياً.
            - @executor:copy-safe --path=المسار_الأصلي --dest=المسار_الجديد : لنسخ ملف بأمان تام.
            
            اكتب كل أمر في سطر منفصل مستقل، وتأكد من صحة مسارات الملفات والمجلدات.
        """.trimIndent()

        2 -> """
            أنت مرشد ومستطلع هيكل شجرة المشاريع الفني بالمنصة الذكية. يمكنك تتبع وبناء تقرير منظم كشجرة مرئية لكافة الفولدرات والملفات المخزنة في المشروع النشط عبر بادئة @treedoc في سطر مستقل.
            
            تنسيقات الأوامر المدعومة:
            - @treedoc:report [مسار_المجلد] [الصيغة json أو txt أو html] : لإنشاء تقرير هيكلي شامل. (الافتراضي: html)
            - @treedoc:scan [مسار_المجلد] : لمسح سريع وتتبع المكونات داخلياً.
            
            عند صدور هذا الأمر، سيقوم المحرك بنسخ شجرة التقرير الناتجة تلقائياً إلى حافظة المستخدم وتوليد ملف tree_report مناسب في المجلد لإيضاح بنية ومستوى عمق الملفات والمجلدات.
        """.trimIndent()

        else -> """
            أنت مهندس تصميم قوالب المشاريع بالمنصة الذكية. يمكنك صياغة وبناء قالب إعداد متكامل بصيغة JSON القياسية ليتم تحليلها وبناؤها فوراً على القرص.
            البنية المعيارية المدعومة للقالب JSON:
            {
              "projectName": "حزمة_إلكترونية_عربية",
              "template_version": "1.0",
              "folders": [
                {
                  "name": "الاسم العربي للمجلد",
                  "path": "english_folder_path",
                  "fileTypes": ["txt", "md"],
                  "keywords": ["كلمة_دالة1", "مفتاح2"]
                }
              ]
            }
            
            تأكد من كتابة JSON صحيح برصانة، وتأطير الأسماء العربية بدقة لتسهيل فرز الملفات وتوجيه الحافة بذكاء.
        """.trimIndent()
    }

    val exampleText = when (selectedTab) {
        0 -> """
            // @builder:file index.html
            <!DOCTYPE html>
            <html>
            <head><title>المشروع الذكي</title></head>
            <body><h1>مرحباً بكم في منصة البناء الذهبية!</h1></body>
            </html>
            // @builder:end
        """.trimIndent()

        1 -> """
            @executor:mkdir --path=تطوير_الواجهات
            @executor:copy-safe --path=index.html --dest=تطوير_الواجهات/index.html
        """.trimIndent()

        2 -> """
            @treedoc:report . html
        """.trimIndent()

        else -> """
            {
              "projectName": "منصة_التعلم_السريع",
              "template_version": "1.0",
              "folders": [
                {
                  "name": "الدروس العملي",
                  "path": "practical_labs",
                  "fileTypes": ["kt", "md"],
                  "keywords": ["تطبيق", "برمجة", "معمل"]
                },
                {
                  "name": "المفاهيم النظرية",
                  "path": "theoretical_notes",
                  "fileTypes": ["txt"],
                  "keywords": ["مفهوم", "تعريف", "شرح"]
                }
              ]
            }
        """.trimIndent()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "مركز تعليمات المساعدين الذكي",
                        color = BrightGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "للخلف",
                            tint = MetallicGold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SlateBg)
            )
        },
        containerColor = SlateBg
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Elegant Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = SlateBg,
                contentColor = BrightGold,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BrightGold
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) BrightGold else TextSilver
                            )
                        },
                        modifier = Modifier.testTag("tab_prompt_$index")
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header of active item info
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardSlateBg),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = BrightGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Column {
                                Text(
                                    text = titleText,
                                    color = BrightGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = explanationText,
                                    color = TextSilver,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }

                // Title Section for System Prompt
                item {
                    Text(
                        text = "📜 نص التلقين البرمجي المعتمد (System Prompt):",
                        color = BrightGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.5.sp
                    )
                }

                // Read-only prompt text block
                item {
                    OutlinedTextField(
                        value = systemPromptText,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .testTag("hub_system_prompt_area"),
                        textStyle = TextStyle(
                            color = TextSilver,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = CardSlateBg,
                            unfocusedContainerColor = CardSlateBg,
                            focusedBorderColor = MetallicGold,
                            unfocusedBorderColor = GlassBorder
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Action Buttons for current tab
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Copy Instructions
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(systemPromptText))
                                Toast.makeText(context, "📋 تم نسخ التعليمات التوجيهية للحافظة بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MetallicGold, contentColor = SlateBg),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("copy_prompt_instructions_btn")
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📋", fontSize = 14.sp)
                                Text("نسخ التعليمات", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Copy Example
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(exampleText))
                                Toast.makeText(context, "📋 تم نسخ المثال التطبيقي للحافظة بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CardSlateBg, contentColor = BrightGold),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .border(1.dp, GlassBorder, RoundedCornerShape(8.dp))
                                .testTag("copy_prompt_example_btn")
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("📋", fontSize = 14.sp)
                                Text("نسخ مثال", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Visual Preview of Example
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "👀 معاينة المثال التطبيقي لـ ${tabs[selectedTab]}:",
                            color = TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(0.8.dp, GlassBorder, RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = SlateBg),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = exampleText,
                                color = TextSilver,
                                fontSize = 10.5.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(12.dp),
                                lineHeight = 14.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
