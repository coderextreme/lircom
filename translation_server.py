from fastapi import FastAPI
from pydantic import BaseModel
from transformers import M2M100ForConditionalGeneration, M2M100Tokenizer
import torch

app = FastAPI()

device = "cuda" if torch.cuda.is_available() else "cpu"
print(f"Loading M2M100 model onto: {device.upper()}...")

# Load the multilingual model
model_name = "facebook/m2m100_418M"
tokenizer = M2M100Tokenizer.from_pretrained(model_name)
model = M2M100ForConditionalGeneration.from_pretrained(model_name).to(device)

print("Model loaded successfully! Server is ready.")

# Update the expected request payload
class TranslationRequest(BaseModel):
    text: str
    source_lang: str  # e.g., "en" for English
    target_lang: str  # e.g., "es" for Spanish

@app.post("/translate")
def translate(req: TranslationRequest):
    try:
        print(req)
        # 1. Set the source language
        tokenizer.src_lang = req.source_lang

        # 2. Tokenize the input text
        encoded = tokenizer(req.text, return_tensors="pt").to(device)

        # 3. Get the token ID for the target language
        target_lang_id = tokenizer.get_lang_id(req.target_lang)

        # 4. Generate translation
        generated_tokens = model.generate(
            **encoded,
            forced_bos_token_id=target_lang_id,
            max_new_tokens=200
        )

        # 5. Decode back to text
        result = tokenizer.batch_decode(generated_tokens, skip_special_tokens=True)[0]
        print(result)

        return {"translation": result}
    except Exception as e:
        return {"error": str(e)}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)
