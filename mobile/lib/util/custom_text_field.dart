import 'package:flutter/material.dart';

class CustomTextFormField extends StatelessWidget {
  final String labelText;
  final ValueChanged<String> onChanged;
  final IconData icon;
  final TextInputType keyboardType;
  final bool obscureText;
  final EdgeInsetsGeometry padding;

  const CustomTextFormField({
    super.key,
    required this.labelText,
    required this.onChanged,
    required this.icon,
    this.padding = const EdgeInsets.only(left: 20, right: 20, bottom: 20),
    this.keyboardType = TextInputType.text,
    this.obscureText = false,
  });

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: padding,
      child: Form(
        child: TextFormField(
          onChanged: onChanged,
          obscureText: obscureText,
          keyboardType: keyboardType,
          decoration: InputDecoration(
            focusedBorder: UnderlineInputBorder(
              borderSide: BorderSide.none,
              borderRadius: BorderRadius.circular(10),
            ),
            enabledBorder: UnderlineInputBorder(
              borderSide: BorderSide.none,
              borderRadius: BorderRadius.circular(10),
            ),
            prefixIcon: Icon(
              icon,
              color: Colors.green,
            ),
            filled: true,
            fillColor: Colors.white,
            labelText: labelText,
          ),
        ),
      ),
    );
  }
}
