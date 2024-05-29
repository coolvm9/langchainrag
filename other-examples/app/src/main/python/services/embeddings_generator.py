from sentence_transformers import SentenceTransformer
import sys
import json

model = SentenceTransformer("all-MiniLM-L6-v2")

def generate_embeddings(sentences):
    sentence_embeddings = model.encode(sentences)
    return sentence_embeddings

if __name__ == "__main__":
    input_sentences = json.loads(sys.argv[1])
    embeddings = generate_embeddings(input_sentences)
    print(json.dumps(embeddings.tolist()))  # Convert numpy arrays to list for JSON serialization
